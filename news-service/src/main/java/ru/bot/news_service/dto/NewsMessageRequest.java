package ru.bot.news_service.dto;

public record NewsMessageRequest(
        Long chatId,
        Long userId,
        String text
) {
}
