package ru.bot.HelperBot.bot.message;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotResponse;

public interface MessageHandler {

    boolean canHandle(Message message);

    BotResponse handle(Message message);
}
