package ru.bot.user_vacancy.user.handler;

import org.springframework.stereotype.Component;
import ru.bot.user_vacancy.common.dto.BotCommand;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.domain.UserState;

@Component
public class AgeHandler implements PersonStateHandler {

    @Override
    public boolean canHandle(UserMessageRequest request, UserSession userSession) {
        return userSession.getUserState() == UserState.WAITING_AGE;
    }

    @Override
    public BotResponse handle(UserMessageRequest request, UserSession userSession) {
        try {
            userSession.setAge(Integer.parseInt(request.text().trim()));
        } catch (NumberFormatException e) {
            return BotResponse.of(BotCommand.sendMessage(request.chatId(), "Введите возраст числом, например 28."));
        }

        userSession.setUserState(UserState.WAITING_VACANCY);
        return BotResponse.of(BotCommand.sendMessage(request.chatId(), "Возраст сохранён. Введите профессию или должность для поиска."));
    }
}
