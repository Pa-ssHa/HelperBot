package ru.bot.news_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import ru.bot.news_service.dto.BotResponse;

@Slf4j
@Component
public class GatewayBotClient {

    private final WebClient webClient;

    public GatewayBotClient(
            WebClient.Builder webClientBuilder,
            @Value("${services.gateway.url:http://localhost:8080}") String gatewayUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(gatewayUrl).build();
    }

    public boolean send(BotResponse botResponse) {
        if (botResponse == null || botResponse.commands() == null || botResponse.commands().isEmpty()) {
            return true;
        }

        try {
            webClient.post()
                    .uri("/internal/v1/bot/commands")
                    .bodyValue(botResponse)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (WebClientException e) {
            log.warn("Unable to send news response through gateway", e);
            return false;
        }
    }
}
