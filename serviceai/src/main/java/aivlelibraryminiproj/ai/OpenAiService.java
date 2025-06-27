package aivlelibraryminiproj.ai;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Service
public class OpenAiService {

    private final String apiKey;
    private final String chatUrl = "https://api.openai.com/v1/chat/completions";
    private final String imageUrl = "https://api.openai.com/v1/images/generations";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public OpenAiService(@Value("${openai.api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * AI에게 title + content를 주고
     * 줄거리, 카테고리(장르)를 JSON 형태로 받는다.
     * (예: {"plot": "...", "category": "로맨스"})
     */
    public Map<String, String> generatePlotAndCategory(String title, String content) throws IOException {
        String prompt = String.format(
                            "너는 책 원고 내용을 보고 다음 JSON 형식으로 줄거리(plot)와 카테고리(category)를 알려주는 AI야.\n" +
                            "JSON 예시: {\"plot\": \"줄거리 내용\", \"category\": \"장르\"}\n" +
                            "다음 내용을 보고 알려줘:\n" +
                            "제목: %s\n" +
                            "내용: %s",
                            title, content
                        );

        Map<String, String> userMessage = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(userMessage),
                "temperature", 0.7
        );

        Request request = new Request.Builder()
                .url(chatUrl)
                .header("Authorization", "Bearer " + apiKey)  // "Bearer " 붙여야함
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

            // AI가 JSON 문자열로 응답하므로 이를 Map으로 파싱
            return mapper.readValue(contentStr, Map.class);
        }
    }

    /**
     * AI 이미지 생성 API 호출
     * prompt: 표지 이미지 생성용 텍스트 (예: "책 제목: ~ 내용: ~")
     * 리턴: 이미지 바이트 배열
     */
    public byte[] generateCoverImage(String prompt) throws IOException {
        Map<String, Object> body = Map.of(
                "prompt", prompt,
                "n", 1,
                "size", "512x512"
        );

        Request request = new Request.Builder()
                .url(imageUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(mapper.writeValueAsString(body), MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String json = response.body().string();
            String imageUrl = mapper.readTree(json).get("data").get(0).get("url").asText();

            // 이미지 URL에서 이미지 바이트 직접 다운로드
            Request imageRequest = new Request.Builder().url(imageUrl).build();
            try (Response imageResponse = client.newCall(imageRequest).execute()) {
                if (!imageResponse.isSuccessful()) {
                    throw new IOException("Failed to download image: " + imageResponse);
                }
                return imageResponse.body().bytes();
            }
        }
    }

    public String saveBytesToFile(byte[] bytes, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            fos.write(bytes);
        }

        return filePath;
    }

    public String saveTextAsPdf(String text, String filePath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(25, 700);

                String[] lines = text.split("\n");
                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
                contentStream.endText();
            }

            document.save(filePath);
        }

        return filePath;
    }

}
