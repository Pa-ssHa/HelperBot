package ru.bot.HelperBot.userVacancy.dto;

public record RateVacancyCallbackRequest(
        Long chatId,
        Long telegramUserId,
        Integer messageId,
        String callbackData
) {
}
