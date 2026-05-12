package ru.bot.HelperBot.bot.dispetcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.document.DocumentDispatcher;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.bot.message.MessageDispatcher;

@Service
@RequiredArgsConstructor
public class GatewayMainDispatcher {

    private final CallbackDispatcher callbackDispatcher;
    private final DocumentDispatcher documentDispatcher;
    private final MessageDispatcher messageDispatcher;

    public BotResponse dispatch(Update update) {
        if (update.hasCallbackQuery()) {
            return callbackDispatcher.dispatch(update.getCallbackQuery());
        }

        if (!update.hasMessage()) {
            return BotResponse.empty();
        }

        if (update.getMessage().hasDocument()) {
            return documentDispatcher.dispatch(update.getMessage());
        }

        if (update.getMessage().hasText()) {
            return messageDispatcher.dispatch(update.getMessage());
        }

        return BotResponse.empty();
    }
}
