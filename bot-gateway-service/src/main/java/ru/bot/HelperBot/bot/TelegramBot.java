package ru.bot.HelperBot.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.bot.HelperBot.bot.dispetcher.GatewayMainDispatcher;
import ru.bot.HelperBot.bot.executor.BotResponseExecutor;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final GatewayMainDispatcher gatewayMainDispatcher;
    private final BotResponseExecutor botResponseExecutor;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    public TelegramBot(GatewayMainDispatcher gatewayMainDispatcher, BotResponseExecutor botResponseExecutor) {
        this.gatewayMainDispatcher = gatewayMainDispatcher;
        this.botResponseExecutor = botResponseExecutor;
    }

    @PostConstruct
    public void init() {
        try {
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
        botResponseExecutor.execute(gatewayMainDispatcher.dispatch(update), this);
    }
}
