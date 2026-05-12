package ru.bot.HelperBot.bot.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentDispatcher {

    private final List<DocumentHandler> handlers;

    public BotResponse dispatch(Message message) {
        if (message == null || !message.hasDocument()) {
            return BotResponse.empty();
        }

        for (DocumentHandler handler : handlers) {
            if (handler.canHandle(message)) {
                return handler.handle(message);
            }
        }

        return BotResponse.empty();
    }
}
