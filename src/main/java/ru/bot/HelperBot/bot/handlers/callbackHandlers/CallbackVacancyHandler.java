package ru.bot.HelperBot.bot.handlers.callbackHandlers;


import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.model.Vacancy;

public interface CallbackVacancyHandler {

    boolean canCallback(CallbackQuery callbackQuery);
    void callback(CallbackQuery callbackQuery, TelegramBot telegramBot);
}
