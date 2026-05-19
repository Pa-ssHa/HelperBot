package ru.bot.news_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bot.news_service.dto.NewsApiResponse;

@Service
public class NewsApiClient {

    private final WebClient webClient;
    private final String apiKey;
    private final String language;
    private final Integer pageSize;

    public NewsApiClient(
            WebClient.Builder builder,
            @Value("${newsapi.url}") String newsApiUrl,
            @Value("${newsapi.api-key}") String apiKey,
            @Value("${newsapi.language:ru}") String language,
            @Value("${newsapi.page-size:5}") Integer pageSize
    ) {
        this.webClient = builder
                .baseUrl(newsApiUrl)
                .build();
        this.apiKey = apiKey;
        this.language = language;
        this.pageSize = pageSize;
    }

    public NewsApiResponse search(String query) {
        return webClient.get()
                .uri(builder -> builder
                        .queryParam("q", query)
                        .queryParam("language", language)
                        .queryParam("sortBy", "publishedAt")
                        .queryParam("pageSize", pageSize)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(NewsApiResponse.class)
                .block();
    }
}
