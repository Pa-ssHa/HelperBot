package ru.bot.HelperBot.bot.handlers.userFormHandlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.user.domain.UserSession;
import ru.bot.HelperBot.user.domain.UserState;

@Component
public class AgeHandler implements PersonStateHandler{
    @Override
    public boolean canHandler(Update update, UserSession userSession) {
        return userSession.getUserState().equals(UserState.WAITING_AGE) &&
                update.hasMessage() &&
                update.getMessage().hasText();
    }

    @Override
    public void handle(Update update, UserSession userSession, TelegramBot telegramBot) {
        userSession.setUserState(UserState.WAITING_VACANCY);
        userSession.setAge(Integer.parseInt(update.getMessage().getText()));
        telegramBot.sendMessage(update.getMessage().getChatId(), "Возраст сохранен. Введите вакансию");
    }
}
