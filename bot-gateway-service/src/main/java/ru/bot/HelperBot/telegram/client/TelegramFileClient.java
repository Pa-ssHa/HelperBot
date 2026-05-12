package ru.bot.HelperBot.telegram.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TelegramFileClient {

    private final WebClient telegramApiClient;
    private final WebClient fileClient;

    @Value("${telegram.bot.token}")
    private String botToken;

    public TelegramFileClient(WebClient.Builder webClientBuilder) {
        this.telegramApiClient = webClientBuilder
                .clone()
                .baseUrl("https://api.telegram.org")
                .build();
        this.fileClient = webClientBuilder
                .clone()
                .baseUrl("https://api.telegram.org")
                .build();
    }

    public byte[] download(String fileId) {
        TelegramFileResponse response = telegramApiClient.get()
                .uri("/bot{token}/getFile?file_id={fileId}", botToken, fileId)
                .retrieve()
                .bodyToMono(TelegramFileResponse.class)
                .block();

        if (response == null || response.result() == null || response.result().filePath() == null) {
            throw new IllegalStateException("Telegram did not return file path");
        }

        return fileClient.get()
                .uri("/file/bot{token}/{filePath}", botToken, response.result().filePath())
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

    private record TelegramFileResponse(TelegramFile result) {
    }

    private record TelegramFile(@JsonProperty("file_path") String filePath) {
    }
}
