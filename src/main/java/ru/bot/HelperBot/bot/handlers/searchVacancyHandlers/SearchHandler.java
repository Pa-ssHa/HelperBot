package ru.bot.HelperBot.bot.handlers.searchVacancyHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.model.Vacancy;
import ru.bot.HelperBot.model.user.UserSession;
import ru.bot.HelperBot.service.VacancyService;
import ru.bot.HelperBot.service.message.MessageVacancyService;

@Component
public class SearchHandler implements SearchVacancyHandler {

    private final VacancyService vacancyService;
    private final MessageVacancyService messageVacancyService;

    @Autowired
    public SearchHandler(VacancyService vacancyService, MessageVacancyService messageVacancyService) {
        this.vacancyService = vacancyService;
        this.messageVacancyService = messageVacancyService;
    }

    @Override
    public boolean canHandler(Update update) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().equalsIgnoreCase("/searchVacancy");
    }

    @Override
    public void handler(Update update, TelegramBot telegramBot) {
        var vacancies = vacancyService.findVacancy(update.getMessage().getChatId(), 5);
        if (vacancies.isEmpty()) {
            telegramBot.sendMessage(update.getMessage().getChatId(), "По вашей профессии ничего не найдено");
            return;
        }
        for (Vacancy vacancy : vacancies) {
            messageVacancyService.sendMessage(update.getMessage().getChatId(), telegramBot, vacancy);
        }
    }
}
