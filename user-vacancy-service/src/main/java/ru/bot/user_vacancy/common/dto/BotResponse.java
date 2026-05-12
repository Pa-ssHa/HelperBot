package ru.bot.user_vacancy.common.dto;

import java.util.List;

public record BotResponse(
        List<BotCommand> commands
) {
    public static BotResponse of(BotCommand... commands) {
        return new BotResponse(List.of(commands));
    }

    public static BotResponse empty() {
        return new BotResponse(List.of());
    }
}
