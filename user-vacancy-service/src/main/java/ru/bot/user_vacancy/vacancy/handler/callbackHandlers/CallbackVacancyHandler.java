package ru.bot.user_vacancy.vacancy.handler.callbackHandlers;

import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.RateVacancyCallbackRequest;

public interface CallbackVacancyHandler {

    boolean canCallback(RateVacancyCallbackRequest request);

    BotResponse callback(RateVacancyCallbackRequest request);
}
