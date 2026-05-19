package ru.bot.HelperBot.news.dto;

public record NewsMessageRequest(
        Long chatId,
        Long userId,
        String text
) {
}
