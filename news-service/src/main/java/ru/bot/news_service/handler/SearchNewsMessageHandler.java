package ru.bot.news_service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bot.news_service.client.UserDetailsClient;
import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsMessageRequest;
import ru.bot.news_service.service.NewsDeliveryService;

@Component
@RequiredArgsConstructor
public class SearchNewsMessageHandler implements NewsMessageHandler {

    private static final String NEWS_COMMAND = "/news";
    private static final String NEWS_COMMAND_STRING = "📰 Показать новости";
    private static final String NEWS_BUTTON = "📰 Новости";

    private final UserDetailsClient userDetailsClient;
    private final NewsDeliveryService newsDeliveryService;

    @Override
    public boolean canHandle(NewsMessageRequest request) {
        return request.text() != null
               && (NEWS_COMMAND.equalsIgnoreCase(request.text().trim())
                   || NEWS_COMMAND_STRING.equalsIgnoreCase(request.text().trim())
                   || NEWS_BUTTON.equalsIgnoreCase(request.text().trim()));
    }

    @Override
    public BotResponse handle(NewsMessageRequest request) {
        String theme = userDetailsClient.findNewsTheme(request.chatId());
        return newsDeliveryService.buildNewsResponse(request.chatId(), theme, false);
    }
}
