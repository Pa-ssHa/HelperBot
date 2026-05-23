package ru.bot.HelperBot.bot.message.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.userVacancy.client.BotUserVacancyServiceClient;
import ru.bot.HelperBot.userVacancy.dto.SearchVacancyRequest;

@Component
@Order(30)
@RequiredArgsConstructor
public class SearchVacancyMessageHandler implements MessageHandler {

    private final BotUserVacancyServiceClient botUserVacancyServiceClient;

    @Override
    public boolean canHandle(Message message) {
        String text = message.getText().trim();

        return "/searchVacancy".equalsIgnoreCase(text)
               || "🔎 Найти вакансии".equalsIgnoreCase(text);
    }

    @Override
    public BotResponse handle(Message message) {
        Long userId = message.getFrom() == null ? null : message.getFrom().getId();
        return botUserVacancyServiceClient.searchVacancies(
                new SearchVacancyRequest(message.getChatId(), userId, message.getText())
        );
    }
}
