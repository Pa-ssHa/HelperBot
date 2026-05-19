package ru.bot.HelperBot.bot.message.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.news.client.NewsClient;
import ru.bot.HelperBot.news.dto.NewsMessageRequest;

@Component
@Order(35)
@RequiredArgsConstructor
public class SearchNewsMessageHandler implements MessageHandler {

    private final NewsClient newsClient;

    @Override
    public boolean canHandle(Message message) {
        return message.getText().trim().equalsIgnoreCase("/sub_news");
    }

    @Override
    public BotResponse handle(Message message) {
        Long userId = message.getFrom() == null ? null : message.getFrom().getId();
        return newsClient.handleNewsMessage(new NewsMessageRequest(message.getChatId(), userId, message.getText()));
    }
}
