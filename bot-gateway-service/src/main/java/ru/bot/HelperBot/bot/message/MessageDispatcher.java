package ru.bot.HelperBot.bot.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageDispatcher {

    private final List<MessageHandler> handlers;

    public BotResponse dispatch(Message message) {
        if (message == null || !message.hasText()) {
            return BotResponse.empty();
        }

        for (MessageHandler handler : handlers) {
            if (handler.canHandle(message)) {
                return handler.handle(message);
            }
        }

        return BotResponse.empty();
    }
}
