package ru.bot.user_vacancy.vacancy.handler.callbackHandlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bot.user_vacancy.common.dto.BotCommand;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.RateVacancyCallbackRequest;
import ru.bot.user_vacancy.userVacancy.service.UserVacancyService;

@Service
@RequiredArgsConstructor
public class CallbackTypeVacancyHandler implements CallbackVacancyHandler{

    private final UserVacancyService userVacancyService;

    @Override
    public boolean canCallback(RateVacancyCallbackRequest request) {
        return request.callbackData().startsWith("favorite_") ||
                request.callbackData().startsWith("hidden_");
    }

    @Override
    public BotResponse callback(RateVacancyCallbackRequest request) {
        String data = request.callbackData();

        if (data.startsWith("favorite_")) {
            Long vacancyId = Long.valueOf(data.substring("favorite_".length()));

            userVacancyService.makeFavoriteVacancy(request.chatId(), vacancyId);
            return BotResponse.of(
                    BotCommand.sendMessage(request.chatId(), "Вакансия добавлена в избранное")
            );
        }

        Long vacancyId = Long.valueOf(data.substring("hidden_".length()));

        userVacancyService.makeHiddenVacancy(request.chatId(), vacancyId);

        return BotResponse.of(
                BotCommand.deleteMessage(request.chatId(), request.messageId())
        );
    }
}
