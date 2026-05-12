package ru.bot.HelperBot.userVacancy.dto;

public record SearchVacancyRequest(
        Long chatId,
        Long userId,
        String text
) {
}
