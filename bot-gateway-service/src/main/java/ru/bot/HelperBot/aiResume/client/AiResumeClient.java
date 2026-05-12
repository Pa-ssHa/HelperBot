package ru.bot.HelperBot.aiResume.client;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AiResumeClient {

    private final WebClient webClient;

    public AiResumeClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .clone()
                .baseUrl("http://ai-resume-service:8081")
                .build();
    }

    public String analyze(byte[] pdfBytes) {
        var file = new ByteArrayResource(pdfBytes) {
            @Override
            public String getFilename() {
                return "resume.pdf";
            }
        };

        return webClient.post()
                .uri("/api/ai/resume/analyze")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(file)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
