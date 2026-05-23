package ru.bot.HelperBot.bot.message.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.news.client.NewsClient;
import ru.bot.HelperBot.news.dto.NewsMessageRequest;

@Component
@Order(20)
@RequiredArgsConstructor
public class NewsMessageHandler implements MessageHandler {

    private final NewsClient newsClient;

    @Override
    public boolean canHandle(Message message) {
        if (message.getText() == null) {
            return false;
        }

        String text = message.getText().trim();

        return "/sub_news".equalsIgnoreCase(text)
               || "🔔 Подписка".equalsIgnoreCase(text)
               || "📰 Подписка на новости".equalsIgnoreCase(text);
    }

    @Override
    public BotResponse handle(Message message) {
        return newsClient.handleMessage(new NewsMessageRequest(
                message.getChatId(),
                message.getFrom().getId(),
                message.getText()
        ));
    }
}
