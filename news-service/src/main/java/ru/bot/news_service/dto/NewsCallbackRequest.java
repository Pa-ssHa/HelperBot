package ru.bot.news_service.dto;

public record NewsCallbackRequest(
        Long chatId,
        Integer messageId,
        String callbackQueryId,
        String callbackData
) {
}
