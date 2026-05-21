package ru.bot.user_vacancy.user.handler;

import org.springframework.stereotype.Component;
import ru.bot.user_vacancy.common.dto.BotCommand;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.domain.UserState;

@Component
public class VacancyHandler implements PersonStateHandler {

    @Override
    public boolean canHandle(UserMessageRequest request, UserSession userSession) {
        return userSession.getUserState() == UserState.WAITING_VACANCY;
    }

    @Override
    public BotResponse handle(UserMessageRequest request, UserSession userSession) {
        userSession.setNameVacancy(request.text().trim());
        userSession.setUserState(UserState.WAITING_SALARY_EXPECTATION);
        return BotResponse.of(BotCommand.sendMessage(
                request.chatId(),
                "Профессия сохранена. Введите зарплатные ожидания числом, например 180000. Если не важно, отправьте 0."
        ));
    }
}
