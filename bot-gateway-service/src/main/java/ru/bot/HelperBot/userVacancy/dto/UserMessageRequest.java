package ru.bot.HelperBot.userVacancy.dto;

public record UserMessageRequest(
        Long chatId,
        Long userId,
        String text
) {
}
