package ru.bot.user_vacancy.vacancy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.RateVacancyCallbackRequest;
import ru.bot.user_vacancy.vacancy.dispetcher.CallbackVacancyDispatcher;

@RestController
@RequiredArgsConstructor
@RequestMapping("internal/v1/vacancy")
public class VacancyCallbackRestController {

    private final CallbackVacancyDispatcher callbackVacancyDispatcher;

    @PostMapping("/rate/callback")
    public ResponseEntity<BotResponse> handlerRateVacancy(@RequestBody RateVacancyCallbackRequest request) {
        return ResponseEntity.ok(callbackVacancyDispatcher.dispatcher(request));
    }
}
