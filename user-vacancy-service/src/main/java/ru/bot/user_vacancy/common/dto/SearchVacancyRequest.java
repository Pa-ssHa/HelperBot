package ru.bot.user_vacancy.common.dto;

public record SearchVacancyRequest(
        Long chatId,
        Long userId,
        String text
) {
}
