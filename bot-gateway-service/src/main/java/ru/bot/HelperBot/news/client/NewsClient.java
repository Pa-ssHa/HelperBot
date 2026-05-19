package ru.bot.HelperBot.news.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.news.dto.NewsMessageRequest;

@Service
public class NewsClient {

    private final WebClient webClient;

    public NewsClient(WebClient.Builder builder, @Value("${services.news.url}") String newsServiceUrl) {
        this.webClient = builder
                .baseUrl(newsServiceUrl)
                .build();
    }

    public BotResponse handleNewsMessage(NewsMessageRequest request) {
        return webClient.post()
                .uri("/internal/v1/news/message")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BotResponse.class)
                .block();
    }
}
