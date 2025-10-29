package ru.bot.HelperBot.bot.handlers.personForm;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.model.user.UserSession;
import ru.bot.HelperBot.model.user.UserState;

@Component
public class NameHandler implements PersonStateHandler{
    @Override
    public boolean canHandler(Update update, UserSession userSession) {
        return userSession.getUserState().equals(UserState.WAITING_NAME) &&
                update.hasMessage() &&
                update.getMessage().hasText();
    }

    @Override
    public void handle(Update update, UserSession userSession, TelegramBot telegramBot) {
        userSession.setUserState(UserState.WAITING_AGE);
        userSession.setName(update.getMessage().getText());
        telegramBot.sendMessage(update.getMessage().getChatId(), "Имя сохранено. Введите возраст");
    }
}
