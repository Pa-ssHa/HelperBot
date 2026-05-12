package ru.bot.user_vacancy.user.handler;

import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.domain.UserSession;

public interface PersonStateHandler {

    boolean canHandle(UserMessageRequest request, UserSession userSession);

    BotResponse handle(UserMessageRequest request, UserSession userSession);
}
