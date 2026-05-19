package ru.bot.news_service.handler;

import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsMessageRequest;

public interface NewsMessageHandler {

    boolean canHandle(NewsMessageRequest request);

    BotResponse handle(NewsMessageRequest request);
}
