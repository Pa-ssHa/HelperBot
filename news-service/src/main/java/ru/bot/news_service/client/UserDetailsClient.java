package ru.bot.news_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class UserDetailsClient {

    private final RestClient restClient;

    public UserDetailsClient(RestClient.Builder builder, @Value("${services.user-vacancy.url}") String userVacancyServiceUrl) {
        this.restClient = builder
                .baseUrl(userVacancyServiceUrl)
                .build();
    }

    public String findNewsTheme(Long chatId) {
        return restClient.get()
                .uri("/internal/v1/user/news-theme/{id}", chatId)
                .retrieve()
                .body(String.class);
    }
}
