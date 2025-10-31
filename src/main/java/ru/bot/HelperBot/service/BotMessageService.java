package ru.bot.HelperBot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bot.HelperBot.bot.TelegramBot;

import java.util.List;

@Service
public class BotMessageService {

    public void sendMainMenu(Long chatId, TelegramBot bot){
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        KeyboardRow row = new KeyboardRow();

        row.add("/my_info");
        keyboard.setKeyboard(List.of(row));
        keyboard.setResizeKeyboard(true);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите действие: ");
        message.setReplyMarkup(keyboard);

        try{
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
