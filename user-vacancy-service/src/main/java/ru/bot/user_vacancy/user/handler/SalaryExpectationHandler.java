package ru.bot.user_vacancy.user.handler;

import org.springframework.stereotype.Component;
import ru.bot.user_vacancy.common.dto.BotCommand;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.domain.UserState;

@Component
public class SalaryExpectationHandler implements PersonStateHandler {

    @Override
    public boolean canHandle(UserMessageRequest request, UserSession userSession) {
        return userSession.getUserState() == UserState.WAITING_SALARY_EXPECTATION;
    }

    @Override
    public BotResponse handle(UserMessageRequest request, UserSession userSession) {
        int desiredSalary;
        try {
            desiredSalary = Integer.parseInt(request.text().trim().replace(" ", ""));
        } catch (NumberFormatException e) {
            return BotResponse.of(BotCommand.sendMessage(
                    request.chatId(),
                    "Введите зарплатные ожидания числом, например 180000. Если не важно, отправьте 0."
            ));
        }

        if (desiredSalary < 0) {
            return BotResponse.of(BotCommand.sendMessage(
                    request.chatId(),
                    "Зарплатные ожидания не могут быть отрицательными. Введите число или 0."
            ));
        }

        userSession.setDesiredSalary(desiredSalary);
        userSession.setUserState(UserState.WAITING_CITY);
        return BotResponse.of(BotCommand.sendMessage(
                request.chatId(),
                "Зарплатные ожидания сохранены. Введите город для поиска работы."
        ));
    }
}
