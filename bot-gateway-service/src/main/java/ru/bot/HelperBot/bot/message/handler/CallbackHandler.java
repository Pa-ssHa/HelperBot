package ru.bot.HelperBot.bot.message.handler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.bot.HelperBot.bot.dto.BotResponse;

public interface CallbackHandler {

    boolean canHandle(CallbackQuery callbackQuery);

    BotResponse handle(CallbackQuery callbackQuery);
}
