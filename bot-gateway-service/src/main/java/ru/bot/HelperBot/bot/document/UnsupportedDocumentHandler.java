package ru.bot.HelperBot.bot.document;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotCommand;
import ru.bot.HelperBot.bot.dto.BotResponse;

@Component
@Order(100)
public class UnsupportedDocumentHandler implements DocumentHandler {

    @Override
    public boolean canHandle(Message message) {
        return true;
    }

    @Override
    public BotResponse handle(Message message) {
        return BotResponse.of(BotCommand.sendMessage(message.getChatId(), "Пожалуйста, отправьте PDF-файл."));
    }
}
