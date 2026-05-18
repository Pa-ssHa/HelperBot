package ru.bot.HelperBot.aiResume.client;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bot.HelperBot.aiResume.dto.ResumeAnalysisResponse;

@Service
public class AiResumeClient {

    private final WebClient webClient;

    public AiResumeClient(WebClient.Builder webClientBuilder) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)
                )
                .build();

        this.webClient = webClientBuilder
                .exchangeStrategies(strategies)
                .clone()
                .baseUrl("http://ai-resume-service:8081")
                .build();
    }

    public ResumeAnalysisResponse analyze(
            byte[] pdfBytes,
            Long chatId,
            String fileName,
            String telegramFileId
    ) {
        ByteArrayResource fileResource = new ByteArrayResource(pdfBytes) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        body.add("chatId", chatId.toString());
        body.add("fileName", fileName);
        body.add("telegramFileId", telegramFileId);

        return webClient.post()
                .uri("/api/ai/resume/analyze")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ResumeAnalysisResponse.class)
                .block();
    }
}
