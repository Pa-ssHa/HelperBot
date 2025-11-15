package ru.bot.HelperBot.bot.handlers.callbackHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.bot.HelperBot.service.UserVacancyService;

@Service
public class CallbackTypeVacancyHandler implements CallbackVacancyHandler{

    private final UserVacancyService userVacancyService;

    @Autowired
    public CallbackTypeVacancyHandler(UserVacancyService userVacancyService) {
        this.userVacancyService = userVacancyService;
    }

    @Override
    public boolean canCallback(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith("favorite_") ||
                callbackQuery.getData().startsWith("hidden_");
    }

    @Override
    public void callback(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().startsWith("favorite_")) {
            userVacancyService.makeFavoriteVacancy(callbackQuery.getMessage().getChatId(),
                    Long.valueOf(callbackQuery.getData().substring(9)));
            return;
        }
        userVacancyService.makeHiddenVacancy(callbackQuery.getMessage().getChatId(),
                Long.valueOf(callbackQuery.getData().substring(7)));
    }
}
