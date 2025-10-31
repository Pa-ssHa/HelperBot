package ru.bot.HelperBot.bot.handlers.personForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.model.user.UserSession;
import ru.bot.HelperBot.model.user.UserState;
import ru.bot.HelperBot.service.UserService;

@Component
public class CityHandler implements PersonStateHandler{

    private final UserService userService;

    @Autowired
    public CityHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean canHandler(Update update, UserSession userSession) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                userSession.getUserState().equals(UserState.WAITING_CITY);
    }

    @Override
    public void handle(Update update, UserSession userSession, TelegramBot telegramBot) {
        userSession.setUserState(UserState.FINISH);
        userSession.setCity(update.getMessage().getText());

        userService.save(userSession);

        telegramBot.sendMessage(update.getMessage().getChatId(), "Город сохранен успешно. Заполнение окончено. Проверьте сохраненную информацию, если что-то неверно, вызовите снова /my_info и заполните анкету сначала.");
        telegramBot.sendMessage(update.getMessage().getChatId(), userSession.toString());

    }
}
