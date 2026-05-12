package ru.bot.user_vacancy.vacancy.dispetcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.RateVacancyCallbackRequest;
import ru.bot.user_vacancy.vacancy.handler.callbackHandlers.CallbackVacancyHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackVacancyDispatcher {

    private final List<CallbackVacancyHandler> handlers;

    public BotResponse dispatcher(RateVacancyCallbackRequest request) {
        for (CallbackVacancyHandler handler : handlers) {
            if (handler.canCallback(request)) {
                return handler.callback(request);
            }
        }
        return BotResponse.empty();
    }
}
