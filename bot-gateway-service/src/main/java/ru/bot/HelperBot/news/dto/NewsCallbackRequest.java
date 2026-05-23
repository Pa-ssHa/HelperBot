package ru.bot.HelperBot.news.dto;

public record NewsCallbackRequest(
        Long chatId,
        Integer messageId,
        String callbackQueryId,
        String callbackData
) {
}
