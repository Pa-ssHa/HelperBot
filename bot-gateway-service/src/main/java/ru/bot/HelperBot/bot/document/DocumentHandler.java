package ru.bot.HelperBot.bot.document;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotResponse;

public interface DocumentHandler {

    boolean canHandle(Message message);

    BotResponse handle(Message message);
}
