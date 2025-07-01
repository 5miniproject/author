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
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.Matrix;

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

    public String saveTextAsPdf(String title, String author, String plot, String content, String filePath) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (PDDocument document = new PDDocument()) {
            PDType0Font font = PDType0Font.load(document, new File("src/main/resources/NanumGothic.ttf"));

            float margin = 40;
            float normalFontSize = 12f;
            float titleFontSize = 18f;
            float headerFontSize = 16f;
            float leading = 16f;

            float pageWidth = PDRectangle.A4.getWidth();
            float pageHeight = PDRectangle.A4.getHeight();
            float usableWidth = pageWidth - 2 * margin;

            List<PDPage> allPages = new ArrayList<>();

            // --- 1. 첫 페이지 생성 ---
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            allPages.add(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setLeading(leading);

            float currentY = pageHeight - margin;
            float startX = margin;
            contentStream.newLineAtOffset(startX, currentY);

            // --- 제목 중앙 정렬 ---
            contentStream.setFont(font, titleFontSize);
            float titleWidth = font.getStringWidth(title) / 1000 * titleFontSize;
            float titleXOffset = (pageWidth - titleWidth) / 2 - startX;
            contentStream.newLineAtOffset(titleXOffset, 0);
            contentStream.showText(title);
            contentStream.newLine();
            currentY -= leading;

            // --- 저자 (6줄 아래 우측 정렬) ---
            contentStream.setFont(font, normalFontSize);
            for (int i = 0; i < 6; i++) {
                contentStream.newLine();
                currentY -= leading;
            }
            float authorWidth = font.getStringWidth(author) / 1000 * normalFontSize;
            float authorXOffset = usableWidth - authorWidth - titleXOffset;
            contentStream.newLineAtOffset(authorXOffset, 0);
            contentStream.showText(author);
            contentStream.newLine();
            currentY -= leading;

            // --- 줄거리 헤더까지 6줄 공백 ---
            for (int i = 0; i < 6; i++) {
                contentStream.newLine();
                currentY -= leading;
            }

            contentStream.endText();
            contentStream.close();

            // --- 줄거리 헤더 ---
            contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
            contentStream.beginText();
            contentStream.setLeading(leading);
            currentY -= leading;
            contentStream.setTextMatrix(Matrix.getTranslateInstance(startX, currentY));
            contentStream.setFont(font, headerFontSize);
            contentStream.showText("줄거리");
            contentStream.newLine();
            currentY -= leading;

            // --- 헤더와 줄거리 사이 공백 2줄 ---
            contentStream.newLine();
            currentY -= leading;
            contentStream.newLine();
            currentY -= leading;

            // --- 줄거리 본문 ---
            contentStream.setFont(font, normalFontSize);
            List<String> summaryLines = splitTextToLines(plot, font, normalFontSize, usableWidth);
            for (String line : summaryLines) {
                if (currentY - leading < margin) break;
                contentStream.showText(line);
                contentStream.newLine();
                currentY -= leading;
            }

            contentStream.endText();
            contentStream.close();

            // --- 2. 내용 시작 (두 번째 페이지부터) ---
            List<String> contentLines = splitTextToLines(content, font, normalFontSize, usableWidth);

            page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            allPages.add(page);

            contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setLeading(leading);
            currentY = pageHeight - margin;
            contentStream.newLineAtOffset(startX, currentY);

            // --- "내용" 헤더 ---
            contentStream.setFont(font, headerFontSize);
            contentStream.showText("내용");
            contentStream.newLine();
            currentY -= leading;
            contentStream.newLine(); // 헤더와 본문 사이 공백 2줄
            currentY -= leading;

            // --- 내용 본문 ---
            contentStream.setFont(font, normalFontSize);
            for (String line : contentLines) {
                if (currentY - leading < margin) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    allPages.add(page);

                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setLeading(leading);
                    currentY = pageHeight - margin;
                    contentStream.newLineAtOffset(startX, currentY);
                    contentStream.setFont(font, normalFontSize);
                }
                contentStream.showText(line);
                contentStream.newLine();
                currentY -= leading;
            }

            contentStream.endText();
            contentStream.close();

            // --- 3. 페이지 번호 추가 ---
            int totalPages = allPages.size();
            for (int i = 0; i < totalPages; i++) {
                PDPage pg = allPages.get(i);
                String pageNumStr = String.format("%d / %d", i + 1, totalPages);
                float pageFontSize = 10f;
                float textWidth = font.getStringWidth(pageNumStr) / 1000 * pageFontSize;
                float centerX = (pageWidth - textWidth) / 2;
                float bottomY = margin / 2;

                PDPageContentStream footer = new PDPageContentStream(document, pg, PDPageContentStream.AppendMode.APPEND, true);
                footer.beginText();
                footer.setFont(font, pageFontSize);
                footer.setTextMatrix(Matrix.getTranslateInstance(centerX, bottomY));
                footer.showText(pageNumStr);
                footer.endText();
                footer.close();
            }

            document.save(file);
        }

        return filePath;
    }

    public List<String> splitTextToLines(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();

        for (String paragraph : text.split("\n")) {
            if (paragraph.trim().isEmpty()) {
                // 빈 줄이면 공백 라인 추가 (문단 구분)
                lines.add("");
                continue;
            }

            StringBuilder lineBuilder = new StringBuilder();
            for (String word : paragraph.split(" ")) {
                String next = lineBuilder.length() == 0 ? word : lineBuilder + " " + word;
                float width = font.getStringWidth(next) / 1000 * fontSize;

                if (width > maxWidth) {
                    // 현재 줄이 넘치면 줄바꿈
                    lines.add(lineBuilder.toString());
                    lineBuilder = new StringBuilder(word);
                } else {
                    lineBuilder = new StringBuilder(next);
                }
            }
            
            if (lineBuilder.length() > 0) {
                lines.add(lineBuilder.toString());
            }
        }

        return lines;
    }

}
