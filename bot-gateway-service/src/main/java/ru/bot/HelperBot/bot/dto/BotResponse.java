package ru.bot.HelperBot.bot.dto;

import java.util.List;

public record BotResponse(
        List<BotCommand> commands
) {
    public static BotResponse empty() {
        return new BotResponse(List.of());
    }
}
