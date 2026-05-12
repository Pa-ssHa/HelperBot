package ru.bot.user_vacancy.user.dispatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.handler.PersonStateHandler;
import ru.bot.user_vacancy.user.redis.RedisSessionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonFormDispatcher {

    private final List<PersonStateHandler> personHandlers;
    private final RedisSessionService redisSessionService;

    public BotResponse dispatch(UserMessageRequest request) {
        if (request.text() == null || request.text().isBlank()) {
            return BotResponse.empty();
        }

        Long chatId = request.chatId();
        UserSession userSession = redisSessionService.getOrCreate(chatId);

        for (PersonStateHandler personHandler : personHandlers) {
            if (personHandler.canHandle(request, userSession)) {
                BotResponse response = personHandler.handle(request, userSession);
                redisSessionService.save(chatId, userSession);
                return response;
            }
        }

        return BotResponse.empty();
    }
}
