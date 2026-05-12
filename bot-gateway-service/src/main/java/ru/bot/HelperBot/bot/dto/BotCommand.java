package ru.bot.HelperBot.bot.dto;

import java.util.List;

public record BotCommand(
        BotCommandType type,
        Long chatId,
        String text,
        Integer messageId,
        String callbackQueryId,
        String parseMode,
        List<List<InlineButton>> inlineKeyboard,
        List<List<String>> replyKeyboard
) {
    public static BotCommand sendMessage(Long chatId, String text) {
        return new BotCommand(BotCommandType.SEND_MESSAGE, chatId, text, null, null, null, null, null);
    }

    public static BotCommand sendMessage(Long chatId, String text, List<List<String>> replyKeyboard) {
        return new BotCommand(BotCommandType.SEND_MESSAGE, chatId, text, null, null, null, null, replyKeyboard);
    }

    public static BotCommand answerCallback(String callbackQueryId) {
        return new BotCommand(BotCommandType.ANSWER_CALLBACK, null, null, null, callbackQueryId, null, null, null);
    }

    public record InlineButton(String text, String callbackData) {
    }
}
