package ru.bot.user_vacancy.user.handler;

import org.springframework.stereotype.Component;
import ru.bot.user_vacancy.common.dto.BotCommand;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.domain.UserState;

@Component
public class FirstNameHandler implements PersonStateHandler {

    @Override
    public boolean canHandle(UserMessageRequest request, UserSession userSession) {
        return userSession.getUserState() == UserState.WAITING_FIRSTNAME;
    }

    @Override
    public BotResponse handle(UserMessageRequest request, UserSession userSession) {
        userSession.setFirstName(request.text().trim());
        userSession.setUserState(UserState.WAITING_LASTNAME);
        return BotResponse.of(BotCommand.sendMessage(request.chatId(), "Имя сохранено. Введите вашу фамилию"));
    }
}
