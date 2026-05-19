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
        if (request.text() == null || !"/searchVacancy".equalsIgnoreCase(request.text().trim())) {
            return BotResponse.empty();
        }

        var user = userService.findByChatId(request.chatId());
        if (user == null || user.getProfession() == null || user.getProfession().isBlank()) {
            return BotResponse.of(BotCommand.sendMessage(request.chatId(), "Сначала заполните анкету командой /my_info"));
        }

        List<Vacancy> vacancies;
        try {
            vacancies = vacancyService.findVacancy(request.chatId());
        } catch (RestClientException e) {
            return BotResponse.of(BotCommand.sendMessage(
                    request.chatId(),
                    "Не удалось получить вакансии от HH. Попробуйте чуть позже."
            ));
        }
        if (vacancies.isEmpty()) {
            return BotResponse.of(BotCommand.sendMessage(request.chatId(), "По вашей профессии ничего не найдено"));
        }

        List<BotCommand> commands = new ArrayList<>();
        for (Vacancy vacancy : vacancies) {
            commands.add(toCommand(request.chatId(), vacancy));
        }

        return new BotResponse(commands);
    }

    private BotCommand toCommand(Long chatId, Vacancy vacancy) {
        String text = String.format(
                "*%s*%n%s%n%s%n%s%n%s",
                vacancy.getNameVacancy(),
                vacancy.getCompany(),
                vacancy.getCity(),
                vacancy.getSalary(),
                "https://hh.ru/vacancy/" + vacancy.getIdVacancy()
        );

        List<List<BotCommand.InlineButton>> keyboard = List.of(
                List.of(
                        new BotCommand.InlineButton("Добавить в избранное", "vacancy:favorite:" + vacancy.getIdVacancy()),
                        new BotCommand.InlineButton("Пропустить вакансию", "vacancy:hidden:" + vacancy.getIdVacancy())
                )
        );

        return BotCommand.sendMessage(chatId, text, "Markdown", keyboard);
    }
}
