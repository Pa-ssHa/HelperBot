package ru.bot.HelperBot.bot.message;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotCommand;
import ru.bot.HelperBot.bot.dto.BotResponse;

@Component
@Order(20)
public class AnalyzeCommandHandler implements MessageHandler {

    @Override
    public boolean canHandle(Message message) {
        return message.getText().trim().equalsIgnoreCase("/analyse");
    }

    @Override
    public BotResponse handle(Message message) {
        return BotResponse.of(BotCommand.sendMessage(
                message.getChatId(),
                "Отправьте PDF-файл с резюме, и я его проанализирую."
        ));
    }
}
