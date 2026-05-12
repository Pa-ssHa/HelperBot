package ru.bot.user_vacancy.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.dispatcher.PersonFormDispatcher;

@Service
@RequiredArgsConstructor
public class UserMessageService {

    private final PersonFormDispatcher personFormDispatcher;

    public BotResponse handle(UserMessageRequest request) {
        return personFormDispatcher.dispatch(request);
    }
}
