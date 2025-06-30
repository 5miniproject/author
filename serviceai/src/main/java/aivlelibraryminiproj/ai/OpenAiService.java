package aivlelibraryminiproj.ai;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class OpenAiService {

    private final String apiKey;
    private final String chatUrl = "https://api.openai.com/v1/chat/completions";

    private final OkHttpClient client = new OkHttpClient.Builder()
                                        .connectTimeout(30, TimeUnit.SECONDS)
                                        .writeTimeout(30, TimeUnit.SECONDS)
                                        .readTimeout(60, TimeUnit.SECONDS)
                                        .build();
    private final ObjectMapper mapper = new ObjectMapper();

    public OpenAiService(@Value("${openai.api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public Map<String, String> generatePlotAndCategory(String title, String content) throws IOException {
        final int MAX_LENGTH = 15000; // 최대 입력 글자 수 제한

        if (content.length() > MAX_LENGTH) {
            content = content.substring(0, MAX_LENGTH);
        }

        String prompt = String.format(
            "너는 책 원고 내용을 보고 핵심적인 줄거리(plot)만 아주 간결하게 5줄 이내로 요약하는 AI야.\n" +
            "그리고 책의 장르(category)는 다음 중에서 하나만 선택해줘: 소설, 에세이, 인문, 경제, 만화.\n" +
            "아래 형식의 JSON으로 결과를 출력해줘:\n" +
            "{\"plot\": \"줄거리 내용\", \"category\": \"장르\"}\n" +
            "제목: %s\n" +
            "내용: %s",
            title, content
        );

        Map<String, String> userMessage = Map.of(
            "role", "user",
            "content", prompt
        );

        Map<String, Object> body = Map.of(
            "model", "gpt-4o",
            "messages", List.of(userMessage),
            "temperature", 0.7
        );

        Request request = new Request.Builder()
            .url(chatUrl)
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .post(RequestBody.create(mapper.writeValueAsString(body), MediaType.get("application/json")))
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String json = response.body().string();
            JsonNode root = mapper.readTree(json);
            String contentStr = root.get("choices").get(0).get("message").get("content").asText();

            String cleanedContentStr = contentStr
                .replaceAll("(?s)```json", "")
                .replaceAll("```", "")
                .trim();

            Map<String, String> result = mapper.readValue(cleanedContentStr, Map.class);

            String plot = result.getOrDefault("plot", "");
            String category = result.getOrDefault("category", "미정");

            return Map.of(
                "plot", plot.trim(),
                "category", category.trim()
            );

        } catch (Exception e) {
            throw new IOException("GPT 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    public byte[] generateCoverImage(String title, String author, String plot, String category) throws IOException {
        if (plot.length() > 400) {
            plot = plot.substring(0, 400) + "...";
        }

        String enTitle = translateText(title);
        String enAuthor = translateText(author);
        String enPlot = translateText(plot);
        String enCategory = translateText(category);

        String prompt = String.format(
            "Create a realistic, printable front book cover illustration showing the title and author clearly.\n" +
            "Title: %s\n" +
            "Author: %s\n" +
            "Genre: %s\n" +
            "Summary: %s\n" +
            "Use only English text for title and author.",
            enTitle, enAuthor, enCategory, enPlot
        );

        System.out.println(prompt);

        Map<String, Object> body = Map.of(
            "model", "dall-e-3",
            "prompt", prompt,
            "n", 1,
            "size", "1024x1024"
        );

        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/images/generations")
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .post(RequestBody.create(mapper.writeValueAsString(body), MediaType.get("application/json")))
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errBody = response.body() != null ? response.body().string() : "no response body";
                throw new IOException("Image generation failed: " + response.code() + " " + response.message() + " Body: " + errBody);
            }

            String json = response.body().string();
            JsonNode dataNode = mapper.readTree(json).get("data");
            if (dataNode == null || !dataNode.isArray() || dataNode.size() == 0) {
                throw new IOException("No image URL returned in response");
            }

            String imageUrl = dataNode.get(0).get("url").asText();

            Request imageRequest = new Request.Builder().url(imageUrl).build();
            try (Response imageResponse = client.newCall(imageRequest).execute()) {
                if (!imageResponse.isSuccessful()) {
                    throw new IOException("Failed to download image: " + imageResponse);
                }
                byte[] originalImageBytes = imageResponse.body().bytes();

                return resizeImageToCoverRatio(originalImageBytes);
            }
        }
    }

    private String translateText(String text) throws IOException {
        String systemMessage = "You are a helpful assistant that translates text into clear, concise English only.\n" +
                                "Do not add any explanations or extra comments.";

        Map<String, Object> userMessage = Map.of(
            "role", "user",
            "content", text
        );
        Map<String, Object> systemMsg = Map.of(
            "role", "system",
            "content", systemMessage
        );

        Map<String, Object> body = Map.of(
            "model", "gpt-4o",
            "messages", List.of(systemMsg, userMessage),
            "temperature", 0.0
        );

        Request request = new Request.Builder()
                .url(chatUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(mapper.writeValueAsString(body), MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String json = response.body().string();
            JsonNode root = mapper.readTree(json);
            String translated = root.get("choices").get(0).get("message").get("content").asText();
            translated = translated.replace("\n", " ").replace("\"", "").trim();
            return translated;
        }
    }


    private byte[] resizeImageToCoverRatio(byte[] originalImageBytes) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));

        int targetWidth = 1024;
        int targetHeight = 1536;

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // 배경 흰색
        g2d.setPaint(java.awt.Color.WHITE);
        g2d.fillRect(0, 0, targetWidth, targetHeight);

        // 이미지 리사이즈 그리기
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        baos.flush();
        byte[] resizedBytes = baos.toByteArray();
        baos.close();

        return resizedBytes;
    }

    public String saveBytesToFile(byte[] bytes, String filePath) throws IOException {
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }

        return filePath;
    }

    public String saveTextAsPdf(String text, String filePath) throws IOException {
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();

                PDType0Font font = PDType0Font.load(document, new File("/workspace/library_project/serviceai/src/main/resources/NanumGothic.ttf"));
                float fontSize = 12;
                contentStream.setFont(font, fontSize);

                contentStream.setLeading(14.5f);
                float margin = 25;
                float width = page.getMediaBox().getWidth() - 2 * margin;
                float startX = margin;
                float startY = page.getMediaBox().getHeight() - margin;

                contentStream.newLineAtOffset(startX, startY);

                List<String> lines = splitTextToLines(text, font, fontSize, width);

                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }

                contentStream.endText();
            }

            document.save(file);
        }

        return filePath;
    }

    private List<String> splitTextToLines(String text, PDType0Font font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] paragraphs = text.split("\n");

        for (String paragraph : paragraphs) {
            StringBuilder line = new StringBuilder();
            for (char c : paragraph.toCharArray()) {
                line.append(c);
                float width = font.getStringWidth(line.toString()) / 1000 * fontSize;
                if (width > maxWidth) {
                    // 글자 하나 전까지 잘라서 줄 추가
                    lines.add(line.substring(0, line.length() - 1));
                    line = new StringBuilder();
                    line.append(c);
                }
            }
            if (line.length() > 0) {
                lines.add(line.toString());
            }
        }
        return lines;
    }

}
