package ru.bot.HelperBot.bot.handlers.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.bot.handlers.userFormHandlers.PersonStateHandler;
import ru.bot.HelperBot.user.domain.UserSession;
import ru.bot.HelperBot.user.redis.RedisSessionService;

import java.util.List;

@Component
public class PersonFormDispatcher {

    private final List<PersonStateHandler> personHandlers;
    private final RedisSessionService redisSessionService;

    @Autowired
    public PersonFormDispatcher(List<PersonStateHandler> personHandlers, RedisSessionService redisSessionService) {
        this.personHandlers = personHandlers;
        this.redisSessionService = redisSessionService;
    }

    public boolean dispatch(Update update, TelegramBot telegramBot){
        Long chatId = update.getMessage().getChatId();
        UserSession userSession = redisSessionService.getOrCreate(chatId);

        for (PersonStateHandler personHandler : personHandlers) {
            if(personHandler.canHandler(update, userSession)){
                personHandler.handle(update, userSession, telegramBot);
                redisSessionService.save(chatId, userSession);
                return true;
            }
        }
        return false;

    }
}
