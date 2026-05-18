package ru.bot.HelperBot.bot.document;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.bot.HelperBot.aiResume.client.AiResumeClient;
import ru.bot.HelperBot.aiResume.dto.ResumeAnalysisResponse;
import ru.bot.HelperBot.bot.dto.BotCommand;
import ru.bot.HelperBot.bot.dto.BotResponse;
import ru.bot.HelperBot.telegram.client.TelegramFileClient;

import java.util.List;

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
            String fileName = message.getDocument().getFileName();
            String telegramFileId = message.getDocument().getFileId();

            byte[] pdfBytes = telegramFileClient.download(telegramFileId);

            ResumeAnalysisResponse response = aiResumeClient.analyze(pdfBytes,
                    message.getChatId(),
                    fileName,
                    telegramFileId);

            return BotResponse.of(
                    BotCommand.sendMessage(message.getChatId(), "Файл получен, анализирую..."),
                    BotCommand.sendMessage(message.getChatId(), formatAnalysis(response))
            );
        } catch (Exception e) {
            e.printStackTrace();
            return BotResponse.of(BotCommand.sendMessage(
                    message.getChatId(),
                    "Ошибка при анализе резюме: " + e.getMessage()
            ));
        }
    }

    private String formatAnalysis(ResumeAnalysisResponse response) {
        return """
            📄 Результат анализа резюме

            ⭐ Оценка: %d/10
            🎯 Соответствие профессии: %d%%

            ✅ Сильные стороны:
            %s

            ⚠️ Слабые стороны:
            %s

            💡 Рекомендации:
            %s
            """.formatted(
                response.getScore(),
                response.getProfession_match(),
                formatList(response.getStrengths()),
                formatList(response.getWeaknesses()),
                formatList(response.getRecommendations())
        );
    }

    private String formatList(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "— Не указано";
        }

        return items.stream()
                .map(item -> "• " + item)
                .reduce((a, b) -> a + "\n" + b)
                .orElse("— Не указано");
    }
}
