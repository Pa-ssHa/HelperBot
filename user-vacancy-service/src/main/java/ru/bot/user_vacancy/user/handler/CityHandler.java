package ru.bot.user_vacancy.user.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bot.user_vacancy.common.dto.BotCommand;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.domain.UserState;
import ru.bot.user_vacancy.user.service.UserService;

@Component
@RequiredArgsConstructor
public class CityHandler implements PersonStateHandler {

    private final UserService userService;

    @Override
    public boolean canHandle(UserMessageRequest request, UserSession userSession) {
        return userSession.getUserState() == UserState.WAITING_CITY;
    }

    @Override
    public BotResponse handle(UserMessageRequest request, UserSession userSession) {
        userSession.setCity(request.text().trim());
        userSession.setUserState(UserState.FINISH);
        userService.save(userSession);

        return BotResponse.of(
                BotCommand.sendMessage(request.chatId(), "Город сохранён. Анкета заполнена, проверьте данные ниже. Если нужно изменить анкету, запустите /my_info ещё раз."),
                BotCommand.sendMessage(request.chatId(), userSession.toString())
        );
    }
}
