package ru.bot.HelperBot.bot.handlers.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.bot.HelperBot.bot.handlers.callbackHandlers.CallbackVacancyHandler;

import java.util.List;

@Service
public class CallbackVacancyDispatcher {

    private final List<CallbackVacancyHandler> handlers;

    @Autowired
    public CallbackVacancyDispatcher(List<CallbackVacancyHandler> handlers) {
        this.handlers = handlers;
    }

    public boolean dispatcher(CallbackQuery callbackQuery) {
        for (CallbackVacancyHandler handler : handlers) {
            if (handler.canCallback(callbackQuery)) {
                handler.callback(callbackQuery);
                return true;
            }
        }
        return false;
    }
}
