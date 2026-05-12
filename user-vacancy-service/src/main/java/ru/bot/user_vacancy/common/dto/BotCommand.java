package ru.bot.user_vacancy.common.dto;


public record BotCommand(
        BotCommandType type,
        Long chatId,
        String text,
        Integer messageId,
        String callbackQueryId
) {
    public static BotCommand sendMessage(Long chatId, String text) {
        return new BotCommand(
                BotCommandType.SEND_MESSAGE,
                chatId,
                text,
                null,
                null
        );
    }

    public static BotCommand deleteMessage(Long chatId, Integer messageId) {
        return new BotCommand(
                BotCommandType.DELETE_MESSAGE,
                chatId,
                null,
                messageId,
                null
        );
    }

    public static BotCommand answerCallback(String callbackId) {
        return new BotCommand(
                BotCommandType.ANSWER_CALLBACK,
                null,
                null,
                null,
                callbackId
        );
    }

}
