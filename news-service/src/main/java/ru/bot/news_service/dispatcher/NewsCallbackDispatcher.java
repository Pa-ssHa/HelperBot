package ru.bot.news_service.dispatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsCallbackRequest;
import ru.bot.news_service.handler.NewsCallbackHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsCallbackDispatcher {

    private final List<NewsCallbackHandler> handlers;

    public BotResponse dispatch(NewsCallbackRequest request) {
        for (NewsCallbackHandler handler : handlers) {
            if (handler.canHandle(request)) {
                return handler.handle(request);
            }
        }

        return BotResponse.empty();
    }
}
