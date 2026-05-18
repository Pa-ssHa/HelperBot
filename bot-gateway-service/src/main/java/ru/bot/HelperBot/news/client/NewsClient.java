package ru.bot.HelperBot.news.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NewsClient {

    private final WebClient webClient;

    public NewsClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8083/news")
                .build();
    }
}
