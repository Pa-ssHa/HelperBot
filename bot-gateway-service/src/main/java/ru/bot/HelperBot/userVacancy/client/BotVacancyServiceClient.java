package ru.bot.HelperBot.userVacancy.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.userVacancy.dto.RateVacancyCallbackRequest;

@Service
public class BotVacancyServiceClient {

    private final WebClient webClient;

    public BotVacancyServiceClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://user-vacancy-service:8082")
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
}
