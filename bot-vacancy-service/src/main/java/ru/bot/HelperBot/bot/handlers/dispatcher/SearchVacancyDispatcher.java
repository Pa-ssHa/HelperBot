package ru.bot.HelperBot.bot.handlers.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.bot.handlers.searchVacancyHandlers.SearchVacancyHandler;
import ru.bot.HelperBot.model.user.UserSession;
import ru.bot.HelperBot.service.RedisSessionService;

import java.util.List;
import java.util.logging.Logger;

@Component
public class SearchVacancyDispatcher {

    private final List<SearchVacancyHandler> handlers;
    private final RedisSessionService redisSessionService;

    private final static Logger log = Logger.getLogger(SearchVacancyDispatcher.class.getName());

    @Autowired
    public SearchVacancyDispatcher(List<SearchVacancyHandler> handlers, RedisSessionService redisSessionService) {
        this.handlers = handlers;
        this.redisSessionService = redisSessionService;
    }

    public boolean dispatch(Update update, TelegramBot telegramBot) {

        log.info("Dispatcher from searchVacancy");

        UserSession userSession = redisSessionService.getOrCreate(update.getMessage().getChatId());

        for (SearchVacancyHandler handler : handlers) {
            if (handler.canHandler(update)) {
                handler.handler(update, telegramBot);
                return true;
            }
        }
        return false;
    }
}
