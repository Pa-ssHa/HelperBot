package ru.bot.user_vacancy.vacancy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import ru.bot.user_vacancy.common.dto.BotCommand;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.SearchVacancyRequest;
import ru.bot.user_vacancy.user.service.UserService;
import ru.bot.user_vacancy.vacancy.domain.Vacancy;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancySearchMessageService {

    private final VacancyService vacancyService;
    private final UserService userService;

    public BotResponse search(SearchVacancyRequest request) {
        if (request.text() == null
            || (!"/searchVacancy".equalsIgnoreCase(request.text().trim())
                && !"🔎 Найти вакансии".equalsIgnoreCase(request.text().trim()))) {
            return BotResponse.empty();
        }

        var user = userService.findByChatId(request.chatId());
        if (user == null || user.getProfession() == null || user.getProfession().isBlank()) {
            return BotResponse.of(BotCommand.sendMessage(
                    request.chatId(),
                    "\u0421\u043d\u0430\u0447\u0430\u043b\u0430 \u0437\u0430\u043f\u043e\u043b\u043d\u0438\u0442\u0435 \u0430\u043d\u043a\u0435\u0442\u0443 \u043a\u043e\u043c\u0430\u043d\u0434\u043e\u0439 /my_info"
            ));
        }

        List<Vacancy> vacancies;
        try {
            vacancies = vacancyService.findVacancy(request.chatId());
        } catch (RestClientException e) {
            return BotResponse.of(BotCommand.sendMessage(
                    request.chatId(),
                    "\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u0432\u0430\u043a\u0430\u043d\u0441\u0438\u0438 \u043e\u0442 HH. \u041f\u043e\u043f\u0440\u043e\u0431\u0443\u0439\u0442\u0435 \u0447\u0443\u0442\u044c \u043f\u043e\u0437\u0436\u0435."
            ));
        }
        if (vacancies.isEmpty()) {
            return BotResponse.of(BotCommand.sendMessage(
                    request.chatId(),
                    "\u041f\u043e \u0432\u0430\u0448\u0435\u0439 \u043f\u0440\u043e\u0444\u0435\u0441\u0441\u0438\u0438 \u043d\u0438\u0447\u0435\u0433\u043e \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u043e"
            ));
        }

        List<BotCommand> commands = new ArrayList<>();
        for (Vacancy vacancy : vacancies) {
            commands.add(toCommand(request.chatId(), vacancy));
        }

        return new BotResponse(commands);
    }

    public BotResponse favorites(SearchVacancyRequest request) {
        if (request.text() == null
            || (!"/favorites".equalsIgnoreCase(request.text().trim())
                && !"⭐ Избранное".equalsIgnoreCase(request.text().trim()))) {
            return BotResponse.empty();
        }

        List<Vacancy> vacancies = vacancyService.findFavoriteVacancies(request.chatId());
        if (vacancies.isEmpty()) {
            return BotResponse.of(BotCommand.sendMessage(
                    request.chatId(),
                    "В избранном пока пусто. Нажмите «В избранное» под подходящей вакансией, и она появится здесь."
            ));
        }

        List<BotCommand> commands = new ArrayList<>();
        commands.add(BotCommand.sendMessage(request.chatId(), "⭐ Ваши избранные вакансии:"));
        vacancies.stream()
                .map(vacancy -> toCommand(request.chatId(), vacancy))
                .forEach(commands::add);

        return new BotResponse(commands);
    }

    public BotCommand toCommand(Long chatId, Vacancy vacancy) {
        String text = String.format(
                "*%s*%n%nКомпания: %s%nГород: %s%nЗарплата: %s%n%n%s",
                escapeMarkdown(vacancy.getNameVacancy()),
                escapeMarkdown(vacancy.getCompany()),
                escapeMarkdown(vacancy.getCity()),
                escapeMarkdown(vacancy.getSalary()),
                "https://hh.ru/vacancy/" + vacancy.getIdVacancy()
        );

        List<List<BotCommand.InlineButton>> keyboard = List.of(
                List.of(
                        new BotCommand.InlineButton("В избранное", "vacancy:favorite:" + vacancy.getIdVacancy()),
                        new BotCommand.InlineButton("Скрыть", "vacancy:hidden:" + vacancy.getIdVacancy())
                )
        );

        return BotCommand.sendMessage(chatId, text, "Markdown", keyboard);
    }

    private String escapeMarkdown(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("*", "\\*")
                .replace("_", "\\_")
                .replace("`", "\\`")
                .replace("[", "\\[");
    }
}
