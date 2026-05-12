package ru.bot.HelperBot.bot.dto;

public record BotCommand(
        BotCommandType type,
        Long chatId,
        String text,
        Integer messageId,
        String callbackQueryId
) {}
