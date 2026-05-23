package ru.bot.news_service.handler;

import org.springframework.stereotype.Component;
import ru.bot.news_service.dto.BotCommand;
import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsMessageRequest;

import java.util.List;

@Component
public class NewsSubscribeMenuHandler implements NewsMessageHandler {

    private static final String NEWS_COMMAND = "/sub_news";
    private static final String NEWS_BUTTON = "🔔 Подписка";
    private static final String OLD_NEWS_BUTTON = "📰 Подписка на новости";

    @Override
    public boolean canHandle(NewsMessageRequest request) {
        if (request.text() == null) {
            return false;
        }

        String text = request.text().trim();

        return NEWS_COMMAND.equalsIgnoreCase(text)
               || NEWS_BUTTON.equalsIgnoreCase(text)
               || OLD_NEWS_BUTTON.equalsIgnoreCase(text);
    }

    @Override
    public BotResponse handle(NewsMessageRequest request) {
        List<List<BotCommand.InlineButton>> keyboard = List.of(
                List.of(
                        new BotCommand.InlineButton("✅ Подписаться", "news:subscribe:yes"),
                        new BotCommand.InlineButton("❌ Отписаться", "news:subscribe:no")
                )
        );

        return BotResponse.of(BotCommand.sendMessage(
                request.chatId(),
                "🔔 Подписка на новости\n\nХотите получать новости по вашей профессии каждый день в 09:00 и 20:00?",
                null,
                keyboard
        ));
    }
}
