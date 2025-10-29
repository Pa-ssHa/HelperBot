package ru.bot.HelperBot.bot.handlers.personForm;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.model.user.UserSession;
import ru.bot.HelperBot.model.user.UserState;

@Component
public class StartHandler implements PersonStateHandler{

    @Override
    public boolean canHandler(Update update, UserSession userSession) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().trim().equalsIgnoreCase("/my_info") &&
                userSession.getUserState().equals(UserState.START);
    }

    @Override
    public void handle(Update update, UserSession userSession, TelegramBot telegramBot) {
        userSession.setUserState(UserState.WAITING_NAME);
        telegramBot.sendMessage(update.getMessage().getChatId(), "Введите имя");
    }
}
