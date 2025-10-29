package ru.bot.HelperBot.bot.handlers.personForm;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.model.user.UserSession;

public interface PersonStateHandler {
    boolean canHandler(Update update, UserSession userSession);
    void handle(Update update, UserSession userSession, TelegramBot telegramBot);
//    default void sendMessage(Update update, TelegramBot telegramBot){
//        SendMessage message = new SendMessage();
//        message.setChatId(update.getMessage().getChatId());
//        message.setText(update.getMessage().getText());
//        try {
//            telegramBot.execute(message);
//        } catch (TelegramApiException e){
//            e.printStackTrace();
//        }
//    }
}
