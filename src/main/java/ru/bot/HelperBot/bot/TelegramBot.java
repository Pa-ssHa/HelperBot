package ru.bot.HelperBot.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.bot.HelperBot.bot.handlers.dispatcher.PersonFormDispatcher;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final PersonFormDispatcher personFormDispatcher;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    public TelegramBot(PersonFormDispatcher personFormDispatcher) {
        this.personFormDispatcher = personFormDispatcher;
    }

    @PostConstruct
    public void init(){
        try{
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("Telegram Bot is registered");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            personFormDispatcher.dispatch(update, this);
        }
    }

    public void sendMessage(Long chatId, String text){
        try{
            execute(new SendMessage(chatId.toString(), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
