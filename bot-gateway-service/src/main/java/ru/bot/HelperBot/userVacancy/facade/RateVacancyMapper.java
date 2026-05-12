package ru.bot.HelperBot.userVacancy.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.bot.HelperBot.userVacancy.dto.RateVacancyCallbackRequest;

@Service
@RequiredArgsConstructor
public class RateVacancyMapper {

    public RateVacancyCallbackRequest toRateVacancyCallbackRequest(CallbackQuery callbackQuery) {
        return new RateVacancyCallbackRequest(
                callbackQuery.getMessage().getChatId(),
                callbackQuery.getFrom().getId(),
                callbackQuery.getMessage().getMessageId(),
                callbackQuery.getData()
                );
    }
}
