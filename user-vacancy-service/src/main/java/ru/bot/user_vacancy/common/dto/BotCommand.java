package ru.bot.user_vacancy.common.dto;

import java.util.List;

public record BotCommand(
        BotCommandType type,
        Long chatId,
        String text,
        Integer messageId,
        String callbackQueryId,
        String parseMode,
        List<List<InlineButton>> inlineKeyboard
) {
    public static BotCommand sendMessage(Long chatId, String text) {
        return new BotCommand(BotCommandType.SEND_MESSAGE, chatId, text, null, null, null, null);
    }

    public static BotCommand sendMessage(Long chatId, String text, String parseMode, List<List<InlineButton>> inlineKeyboard) {
        return new BotCommand(BotCommandType.SEND_MESSAGE, chatId, text, null, null, parseMode, inlineKeyboard);
    }

    public static BotCommand deleteMessage(Long chatId, Integer messageId) {
        return new BotCommand(BotCommandType.DELETE_MESSAGE, chatId, null, messageId, null, null, null);
    }

    public static BotCommand answerCallback(String callbackId) {
        return new BotCommand(BotCommandType.ANSWER_CALLBACK, null, null, null, callbackId, null, null);
    }

    public record InlineButton(String text, String callbackData) {
    }
}
