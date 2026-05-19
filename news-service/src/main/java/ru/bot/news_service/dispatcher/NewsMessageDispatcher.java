package ru.bot.news_service.dispatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsMessageRequest;
import ru.bot.news_service.handler.NewsMessageHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsMessageDispatcher {

    private final List<NewsMessageHandler> handlers;

    public BotResponse dispatch(NewsMessageRequest request) {
        for (NewsMessageHandler handler : handlers) {
            if (handler.canHandle(request)) {
                return handler.handle(request);
            }
        }

        return BotResponse.empty();
    }
}
