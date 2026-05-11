package ru.bot.HelperBot.bot.handlers.userFormHandlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.user.domain.UserSession;

public interface PersonStateHandler {
    boolean canHandler(Update update, UserSession userSession);
    void handle(Update update, UserSession userSession, TelegramBot telegramBot);
}
