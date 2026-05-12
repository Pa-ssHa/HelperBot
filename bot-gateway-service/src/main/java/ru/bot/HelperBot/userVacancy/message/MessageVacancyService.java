package ru.bot.HelperBot.userVacancy.message;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bot.HelperBot.bot.TelegramBot;
import ru.bot.HelperBot.vacancy.domain.Vacancy;

import java.util.List;

@Service
public class MessageVacancyService {

    public void sendMessage(Long chatId, TelegramBot bot, Vacancy vacancy){

        String text = String.format(
                "💼 *%s*\n" +
                        "🏢 %s\n" +
                        "📍 %s\n" +
                        "💰 %s\n" +
                " %s",
                vacancy.getNameVacancy(),
                vacancy.getCompany(),
                vacancy.getCity(),
                vacancy.getSalary(),
                "https://hh.ru/vacancy/" + vacancy.getIdVacancy()
        );

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton favoriteBtn = new InlineKeyboardButton();
        InlineKeyboardButton hiddenBtn = new InlineKeyboardButton();

        favoriteBtn.setText("Добавить в избранное");
        favoriteBtn.setCallbackData("favorite_" + vacancy.getIdVacancy());

        hiddenBtn.setText("Пропустить вакансию");
        hiddenBtn.setCallbackData("hidden_" + vacancy.getIdVacancy());

        markup.setKeyboard(List.of(List.of(favoriteBtn, hiddenBtn)));

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("Markdown");
        message.setReplyMarkup(markup);

        try{
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
