package ru.bot.HelperBot.bot.dispetcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.dto.BotResponse;

@Service
@RequiredArgsConstructor
public class GatewayMainDispatcher {

    private final CallbackDispatcher callbackDispatcher;

    public BotResponse dispatch(Update update){

        if (update.hasCallbackQuery()){
            return callbackDispatcher.dispatch(update.getCallbackQuery());
        }

        return null;
    }
}
