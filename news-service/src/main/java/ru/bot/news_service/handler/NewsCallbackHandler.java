package ru.bot.news_service.handler;

import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsCallbackRequest;

public interface NewsCallbackHandler {

    boolean canHandle(NewsCallbackRequest request);

    BotResponse handle(NewsCallbackRequest request);
}
