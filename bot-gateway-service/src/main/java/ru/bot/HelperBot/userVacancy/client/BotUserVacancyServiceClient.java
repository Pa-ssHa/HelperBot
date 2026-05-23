package ru.bot.HelperBot.userVacancy.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.userVacancy.dto.RateVacancyCallbackRequest;
import ru.bot.HelperBot.userVacancy.dto.SearchVacancyRequest;
import ru.bot.HelperBot.userVacancy.dto.UserMessageRequest;

@Service
public class BotUserVacancyServiceClient {

    private final WebClient webClient;

    public BotUserVacancyServiceClient(
            WebClient.Builder builder,
            @Value("${services.user-vacancy.url}") String userVacancyServiceUrl
    ) {
        this.webClient = builder
                .baseUrl(userVacancyServiceUrl)
                .build();
    }

    public BotResponse handleRateVacancy(RateVacancyCallbackRequest request) {
        return webClient.post()
                .uri("/internal/v1/vacancy/rate/callback")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BotResponse.class)
                .block();
    }

    public BotResponse searchVacancies(SearchVacancyRequest request) {
        return webClient.post()
                .uri("/internal/v1/vacancy/search")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BotResponse.class)
                .block();
    }

    public BotResponse favoriteVacancies(SearchVacancyRequest request) {
        return webClient.post()
                .uri("/internal/v1/vacancy/favorites")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BotResponse.class)
                .block();
    }

    public BotResponse handleUserMessage(UserMessageRequest request) {
        return webClient.post()
                .uri("/internal/v1/user/message")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BotResponse.class)
                .block();
    }
}
