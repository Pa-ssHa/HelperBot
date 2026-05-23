package ru.bot.news_service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bot.news_service.client.UserDetailsClient;
import ru.bot.news_service.dto.BotCommand;
import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsCallbackRequest;
import ru.bot.news_service.service.NewsSubscriptionService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NewsSubscribeCallbackHandler implements NewsCallbackHandler {

    private final NewsSubscriptionService newsSubscriptionService;
    private final UserDetailsClient userDetailsClient;

    @Override
    public boolean canHandle(NewsCallbackRequest request) {
        return request.callbackData() != null
               && request.callbackData().startsWith("news:subscribe:");
    }

    @Override
    public BotResponse handle(NewsCallbackRequest request) {
        String action = request.callbackData().substring("news:subscribe:".length());

        if ("no".equals(action)) {
            newsSubscriptionService.unsubscribe(request.chatId());

            return new BotResponse(List.of(
                    BotCommand.answerCallback(request.callbackQueryId()),
                    BotCommand.sendMessage(request.chatId(), "❌ Подписка на новости отключена.")
            ));
        }

        String theme = userDetailsClient.findNewsTheme(request.chatId());

        if (theme == null || theme.isBlank()) {
            return new BotResponse(List.of(
                    BotCommand.answerCallback(request.callbackQueryId()),
                    BotCommand.sendMessage(request.chatId(), "Сначала заполните профессию через /my_info.")
            ));
        }

        newsSubscriptionService.subscribe(request.chatId(), theme);

        return new BotResponse(List.of(
                BotCommand.answerCallback(request.callbackQueryId()),
                BotCommand.sendMessage(
                        request.chatId(),
                        "✅ Подписка включена.\n\nТема: " + theme + "\nНовости будут приходить каждый день в 09:00 и 20:00."
                )
        ));
    }
}
