package ru.bot.HelperBot.bot.dispetcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.bot.HelperBot.bot.dto.BotCommand;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.userVacancy.client.BotUserVacancyServiceClient;
import ru.bot.HelperBot.userVacancy.facade.RateVacancyMapper;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CallbackDispatcher {


    private final BotUserVacancyServiceClient botUserVacancyServiceClient;
    private final RateVacancyMapper rateVacancyMapper;

    public BotResponse dispatch(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();

        if (data.startsWith("vacancy:")) {
            BotResponse response = botUserVacancyServiceClient.handleRateVacancy(rateVacancyMapper.toRateVacancyCallbackRequest(callbackQuery));
            var commands = new ArrayList<BotCommand>();
            if (response != null && response.commands() != null) {
                commands.addAll(response.commands());
            }
            commands.add(BotCommand.answerCallback(callbackQuery.getId()));
            return new BotResponse(commands);
        }
        return BotResponse.empty();
    }
}
