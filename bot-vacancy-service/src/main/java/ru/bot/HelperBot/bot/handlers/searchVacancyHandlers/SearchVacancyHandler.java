package ru.bot.HelperBot.bot.handlers.searchVacancyHandlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.model.user.UserSession;

public interface SearchVacancyHandler {
    boolean canHandler(Update update);
    void handler(Update update, TelegramBot telegramBot);
}
