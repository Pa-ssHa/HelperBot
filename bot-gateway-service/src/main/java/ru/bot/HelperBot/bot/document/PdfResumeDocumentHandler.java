package ru.bot.HelperBot.bot.document;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.aiResume.client.AiResumeClient;
import ru.bot.HelperBot.bot.dto.BotCommand;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.telegram.client.TelegramFileClient;

@Component
@Order(10)
@RequiredArgsConstructor
public class PdfResumeDocumentHandler implements DocumentHandler {

    private final TelegramFileClient telegramFileClient;
    private final AiResumeClient aiResumeClient;

    @Override
    public boolean canHandle(Message message) {
        String fileName = message.getDocument().getFileName();
        return fileName != null && fileName.toLowerCase().endsWith(".pdf");
    }

    @Override
    public BotResponse handle(Message message) {
        try {
            byte[] pdfBytes = telegramFileClient.download(message.getDocument().getFileId());
            String response = aiResumeClient.analyze(pdfBytes);
            return BotResponse.of(
                    BotCommand.sendMessage(message.getChatId(), "Файл получен, анализирую..."),
                    BotCommand.sendMessage(message.getChatId(), "Результат анализа:\n\n" + response)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return BotResponse.of(BotCommand.sendMessage(
                    message.getChatId(),
                    "Ошибка при анализе резюме: " + e.getMessage()
            ));
        }
    }
}
