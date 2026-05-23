package ru.bot.HelperBot.bot.message.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.news.client.NewsClient;
import ru.bot.HelperBot.news.dto.NewsCallbackRequest;

@Component
@RequiredArgsConstructor
public class NewsCallbackHandler implements CallbackHandler {

    private final NewsClient newsClient;

    @Override
    public boolean canHandle(CallbackQuery callbackQuery) {
        return callbackQuery.getData() != null
               && callbackQuery.getData().startsWith("news:");
    }

    @Override
    public BotResponse handle(CallbackQuery callbackQuery) {
        return newsClient.handleCallback(new NewsCallbackRequest(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getMessage().getMessageId(),
                callbackQuery.getId(),
                callbackQuery.getData()
        ));
    }
}
