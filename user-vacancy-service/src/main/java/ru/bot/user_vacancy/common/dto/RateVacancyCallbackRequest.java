package ru.bot.user_vacancy.common.dto;

public record RateVacancyCallbackRequest(
        Long chatId,
        Long telegramUserId,
        Integer messageId,
        String callbackData
) {
}
