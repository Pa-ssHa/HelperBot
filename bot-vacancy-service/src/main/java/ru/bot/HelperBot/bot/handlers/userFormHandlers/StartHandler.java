package ru.bot.HelperBot.bot.handlers.userFormHandlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.user.domain.UserSession;
import ru.bot.HelperBot.user.domain.UserState;

@Component
public class StartHandler implements PersonStateHandler{

    @Override
    public boolean canHandler(Update update, UserSession userSession) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().trim().equalsIgnoreCase("/my_info") &&
                (userSession.getUserState().equals(UserState.START)
                        || userSession.getUserState().equals(UserState.FINISH));
    }

    @Override
    public void handle(Update update, UserSession userSession, TelegramBot telegramBot) {
        userSession.setUserState(UserState.WAITING_FIRSTNAME);
        telegramBot.sendMessage(update.getMessage().getChatId(), "Введите имя");
    }
}
