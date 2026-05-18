package ru.bot.HelperBot.bot.message.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.userVacancy.client.BotUserVacancyServiceClient;
import ru.bot.HelperBot.userVacancy.dto.UserMessageRequest;

@Component
@Order(100)
@RequiredArgsConstructor
public class UserMessageProxyHandler implements MessageHandler {

    private final BotUserVacancyServiceClient botUserVacancyServiceClient;

    @Override
    public boolean canHandle(Message message) {
        return true;
    }

    @Override
    public BotResponse handle(Message message) {
        Long userId = message.getFrom() == null ? null : message.getFrom().getId();
        return botUserVacancyServiceClient.handleUserMessage(
                new UserMessageRequest(message.getChatId(), userId, message.getText())
        );
    }
}
