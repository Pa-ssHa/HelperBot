package ru.bot.HelperBot.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.bot.HelperBot.bot.handlers.dispatcher.CallbackVacancyDispatcher;
import ru.bot.HelperBot.bot.handlers.dispatcher.PersonFormDispatcher;
import ru.bot.HelperBot.bot.handlers.dispatcher.SearchVacancyDispatcher;
import ru.bot.HelperBot.service.message.MessageInfoService;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final PersonFormDispatcher personFormDispatcher;
    private final MessageInfoService messageInfoService;
    private final SearchVacancyDispatcher searchVacancyDispatcher;
    private final CallbackVacancyDispatcher callbackVacancyDispatcher;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    public TelegramBot(PersonFormDispatcher personFormDispatcher, MessageInfoService messageInfoService, SearchVacancyDispatcher searchVacancyDispatcher, CallbackVacancyDispatcher callbackVacancyDispatcher) {
        this.personFormDispatcher = personFormDispatcher;
        this.messageInfoService = messageInfoService;
        this.searchVacancyDispatcher = searchVacancyDispatcher;
        this.callbackVacancyDispatcher = callbackVacancyDispatcher;
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

        if (update.hasCallbackQuery()) {
            sendAcknowledgmentCallback(update.getCallbackQuery().getId());
            if (callbackVacancyDispatcher.dispatcher(update.getCallbackQuery(), this)) {
                return;
            }
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().equalsIgnoreCase("/start")) {
                sendMessage(update.getMessage().getChatId(), "Привет ," + update.getMessage().getFrom().getFirstName() +
                        ", это бот для поиска вакансий, новостей и здесь есть возможность проверить твое резюме. Для заполнения личной информации вызови: /my_info");
                messageInfoService.sendMainMenu(update.getMessage().getChatId(), this);
                return;
            }
            if (personFormDispatcher.dispatch(update, this)) {
                return;
            } else if (searchVacancyDispatcher.dispatch(update, this)) {
                return;
            }

        }
    }

    public void sendAcknowledgmentCallback(String callback) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callback);
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Long chatId, String text) {
        try {
            execute(new SendMessage(chatId.toString(), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(Long chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try{
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
