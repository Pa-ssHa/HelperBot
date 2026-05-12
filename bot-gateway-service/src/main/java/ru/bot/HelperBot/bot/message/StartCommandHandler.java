package ru.bot.HelperBot.bot.message;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.bot.dto.BotCommand;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.userVacancy.message.MessageInfoService;

@Component
@Order(10)
@RequiredArgsConstructor
public class StartCommandHandler implements MessageHandler {

    private final MessageInfoService messageInfoService;

    @Override
    public boolean canHandle(Message message) {
        return message.getText().trim().equalsIgnoreCase("/start");
    }

    @Override
    public BotResponse handle(Message message) {
        String firstName = message.getFrom() == null ? "" : message.getFrom().getFirstName();
        return BotResponse.of(
                BotCommand.sendMessage(
                        message.getChatId(),
                        "Привет, " + firstName + ", это бот для поиска вакансий, новостей и проверки резюме. Для заполнения личной информации вызови /my_info"
                ),
                BotCommand.sendMessage(message.getChatId(), "Выберите действие:", messageInfoService.mainMenu())
        );
    }
}
