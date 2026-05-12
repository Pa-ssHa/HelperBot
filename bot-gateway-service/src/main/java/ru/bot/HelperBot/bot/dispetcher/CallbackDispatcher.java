package ru.bot.HelperBot.bot.dispetcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.userVacancy.client.BotVacancyServiceClient;
import ru.bot.HelperBot.userVacancy.facade.RateVacancyMapper;

@Service
@RequiredArgsConstructor
public class CallbackDispatcher {


    private final BotVacancyServiceClient botVacancyServiceClient;
    private final RateVacancyMapper rateVacancyMapper;

    public BotResponse dispatch(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();

        if (data.startsWith("vacancy:")) {
            return botVacancyServiceClient.handleRateVacancy(rateVacancyMapper.toRateVacancyCallbackRequest(callbackQuery));
        }
        return null;
    }
}
