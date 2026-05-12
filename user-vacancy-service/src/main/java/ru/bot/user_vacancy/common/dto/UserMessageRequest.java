package ru.bot.user_vacancy.common.dto;

public record UserMessageRequest(
        Long chatId,
        Long userId,
        String text
) {
}
