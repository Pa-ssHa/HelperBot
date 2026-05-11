package ru.bot.HelperBot.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.bot.HelperBot.bot.handlers.dispatcher.CallbackVacancyDispatcher;
import ru.bot.HelperBot.bot.handlers.dispatcher.PersonFormDispatcher;
import ru.bot.HelperBot.bot.handlers.dispatcher.SearchVacancyDispatcher;
import ru.bot.HelperBot.message.MessageInfoService;

import java.util.logging.Logger;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final PersonFormDispatcher personFormDispatcher;
    private final MessageInfoService messageInfoService;
    private final SearchVacancyDispatcher searchVacancyDispatcher;
    private final CallbackVacancyDispatcher callbackVacancyDispatcher;

    private final static Logger log = Logger.getLogger(TelegramBot.class.getName());

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final WebClient aiServiceClient;
    private final WebClient telegramFileClient;

    @Autowired
    public TelegramBot(PersonFormDispatcher personFormDispatcher, MessageInfoService messageInfoService,
                       SearchVacancyDispatcher searchVacancyDispatcher, CallbackVacancyDispatcher callbackVacancyDispatcher, WebClient.Builder webClientBuilder) {
        this.personFormDispatcher = personFormDispatcher;
        this.messageInfoService = messageInfoService;
        this.searchVacancyDispatcher = searchVacancyDispatcher;
        this.callbackVacancyDispatcher = callbackVacancyDispatcher;
        this.aiServiceClient = webClientBuilder
                .clone() // ← Клонируем
                .baseUrl("http://ai-resume-service:8081")
                .build();
        this.telegramFileClient = webClientBuilder
                .clone() // ← Клонируем
                .baseUrl("https://api.telegram.org")
                .build();
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
        if (update.hasMessage()) {
            var message = update.getMessage();

            // Проверяем сначала документ
            if (message.hasDocument()) {
                var document = message.getDocument();
                String fileId = document.getFileId();
                String fileName = document.getFileName();

                if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
                    Long chatId = message.getChatId();
                    sendMessage(chatId, "Файл получен, анализирую...");
                    analyzeResume(chatId, fileId);
                } else {
                    sendMessage(message.getChatId(), "Пожалуйста, отправь PDF-файл.");
                }
                return;
            }

            // Потом проверяем текст
            if (message.hasText()) {
                String messageText = message.getText();

                log.info(messageText);
                System.out.println(messageText);

                Long chatId = message.getChatId();

                log.info(chatId.toString());

                if (messageText.equalsIgnoreCase("/analyse")) {
                    sendMessage(chatId, "Отправь мне PDF-файл с твоим резюме, и я его проанализирую.");
                    return;
                }

                if (messageText.equalsIgnoreCase("/start")) {
                    sendMessage(chatId, "Привет ," + message.getFrom().getFirstName() +
                            ", это бот для поиска вакансий, новостей и здесь есть возможность проверить твое резюме. Для заполнения личной информации вызови: /my_info");
                    System.out.println("вошел в старт");
                    messageInfoService.sendMainMenu(chatId, this);
                    return;
                }

                if (personFormDispatcher.dispatch(update, this)) {
                    System.out.println("вошел в анкету");
                    return;
                }
                if (searchVacancyDispatcher.dispatch(update, this)) {
                    System.out.println("вошел в поиск");
                    return;
                }
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

    // ////////////////////////////
    private byte[] downloadMyFile(String fileId) throws TelegramApiException {
        File file = execute(GetFile.builder()
                .fileId(fileId)
                .build());

        String fileUrl = file.getFileUrl(botToken);

        return telegramFileClient
                .get()
                .uri(fileUrl)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

    // Метод для отправки PDF в ai-resume-service
    private void analyzeResume(Long chatId, String fileId) {
        try {
            byte[] pdfBytes = downloadMyFile(fileId);

            // Создаём multipart/form-data
            var file = new ByteArrayResource(pdfBytes) {
                @Override
                public String getFilename() {
                    return "resume.pdf";
                }
            };

            String response = aiServiceClient.post()
                    .uri("/api/ai/resume/analyze")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(file)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            sendMessage(chatId, "Результат анализа:\n\n" + response);

        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(chatId, "Ошибка при анализе резюме: " + e.getMessage());
        }
    }
}