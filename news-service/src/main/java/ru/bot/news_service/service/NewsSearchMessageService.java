package ru.bot.news_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bot.news_service.dispatcher.NewsMessageDispatcher;
import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsMessageRequest;

@Service
@RequiredArgsConstructor
public class NewsSearchMessageService {

    private final NewsMessageDispatcher newsMessageDispatcher;

    public BotResponse handle(NewsMessageRequest request) {
        return newsMessageDispatcher.dispatch(request);
    }
}
