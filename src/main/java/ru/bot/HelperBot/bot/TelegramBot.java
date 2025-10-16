package ru.bot.HelperBot.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

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
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if("/start".equalsIgnoreCase(text.trim())){
                sendText(chatId, "Hi! I am test bot. Use /ping for checking");
                return;
            }

            if ("/ping".equalsIgnoreCase(text.trim())){
                sendText(chatId, "Pong!");
                return;
            }

            sendText(chatId, "You was write: " + text);
        }
    }

    private void sendText(Long chatId, String text){
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText(text);
        try{
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
