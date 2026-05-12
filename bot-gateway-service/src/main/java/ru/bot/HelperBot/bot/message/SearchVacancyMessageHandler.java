package ru.bot.HelperBot.bot.message;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.userVacancy.client.BotVacancyServiceClient;
import ru.bot.HelperBot.userVacancy.dto.SearchVacancyRequest;

@Component
@Order(30)
@RequiredArgsConstructor
public class SearchVacancyMessageHandler implements MessageHandler {

    private final BotVacancyServiceClient botVacancyServiceClient;

    @Override
    public boolean canHandle(Message message) {
        return message.getText().trim().equalsIgnoreCase("/searchVacancy");
    }

    @Override
    public BotResponse handle(Message message) {
        Long userId = message.getFrom() == null ? null : message.getFrom().getId();
        return botVacancyServiceClient.searchVacancies(
                new SearchVacancyRequest(message.getChatId(), userId, message.getText())
        );
    }
}
