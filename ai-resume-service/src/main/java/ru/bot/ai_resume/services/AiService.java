package ru.bot.ai_resume.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bot.ai_resume.dto.ResumeAnalysisResponse;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {

    private final WebClient webClient;

    public AiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://ollama:11434")
                .build();
    }

    public JsonNode generateResumeAnalysis(String resumeText) {
        String prompt = """
                Ты — HR-эксперт и технический интервьюер.                
                Проанализируй резюме кандидата и оцени его.
                Ответь СТРОГО в формате JSON, без пояснений и без текста вне JSON.
                Формат ответа:
                {
                  "score": number (1-10),
                  "profession_match": number (0-100),
                  "strengths": [string],
                  "weaknesses": [string],
                  "recommendations": [string]
                }
                Резюме:
                %s
                """.formatted(resumeText);

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "ilyagusev/saiga_nemo_12b:latest");
        payload.put("prompt", prompt);
        payload.put("stream", false);

        try {
            JsonNode response = webClient.post()
                    .uri("/api/generate")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null && response.has("response")) {
                return response;
            } else {
                throw new RuntimeException("Некорректный ответ от Ollama");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка вызова Ollama: " + e.getMessage(), e);
        }
    }

    public String generateResponse(String userQuery) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "ilyagusev/saiga_nemo_12b:latest");
        payload.put("prompt", userQuery);
        payload.put("stream", false);

        try {
            JsonNode response = webClient.post()
                    .uri("/api/generate")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null && response.has("response")) {
                return response.get("response").asText();
            } else {
                throw new RuntimeException("Некорректный ответ от Ollama");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка вызова Ollama: " + e.getMessage(), e);
        }
    }
}
