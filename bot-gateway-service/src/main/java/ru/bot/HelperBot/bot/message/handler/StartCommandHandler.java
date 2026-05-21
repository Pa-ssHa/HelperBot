package ru.bot.HelperBot.bot.message.handler;

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
        String greeting = firstName == null || firstName.isBlank()
                ? "Привет!"
                : "Привет, " + firstName + "!";

        return BotResponse.of(
                BotCommand.sendMessage(
                        message.getChatId(),
                        greeting + "\n\nЯ помогу искать вакансии, следить за новыми публикациями, читать новости по профессии и проверять резюме.\n\nНачните с /my_info, чтобы заполнить анкету."
                ),
                BotCommand.sendMessage(message.getChatId(), "Выберите действие:", messageInfoService.mainMenu())
        );
    }
}
