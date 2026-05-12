package ru.bot.user_vacancy.user.handler;

import org.springframework.stereotype.Component;
import ru.bot.user_vacancy.common.dto.BotCommand;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.domain.UserState;

@Component
public class StartHandler implements PersonStateHandler {

    @Override
    public boolean canHandle(UserMessageRequest request, UserSession userSession) {
        return request.text().trim().equalsIgnoreCase("/my_info")
                && (userSession.getUserState() == UserState.START
                || userSession.getUserState() == UserState.FINISH);
    }

    @Override
    public BotResponse handle(UserMessageRequest request, UserSession userSession) {
        userSession.setUserState(UserState.WAITING_FIRSTNAME);
        return BotResponse.of(BotCommand.sendMessage(request.chatId(), "Введите имя"));
    }
}
