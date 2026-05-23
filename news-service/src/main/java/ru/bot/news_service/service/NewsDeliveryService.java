package ru.bot.news_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bot.news_service.client.NewsApiClient;
import ru.bot.news_service.dto.BotCommand;
import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsApiResponse;
import ru.bot.news_service.dto.NewsArticleDto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NewsDeliveryService {

    private static final int RESPONSE_ARTICLES_LIMIT = 2;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final NewsApiClient newsApiClient;
    private final NewsHistoryService newsHistoryService;

    public BotResponse buildNewsResponse(Long chatId, String theme, boolean scheduled) {
        if (theme == null || theme.isBlank()) {
            return BotResponse.of(BotCommand.sendMessage(chatId, "Сначала заполните профессию через /my_info."));
        }

        NewsApiResponse response = newsApiClient.search(theme);
        if (response == null) {
            return BotResponse.of(BotCommand.sendMessage(chatId, "Не удалось получить новости. Попробуйте позже."));
        }

        if (!"ok".equalsIgnoreCase(response.status())) {
            String message = response.message() == null ? "News API вернул ошибку" : response.message();
            return BotResponse.of(BotCommand.sendMessage(chatId, message));
        }

        if (response.articles() == null || response.articles().isEmpty()) {
            return BotResponse.of(BotCommand.sendMessage(chatId, "По вашей профессии новости не найдены."));
        }

        Set<String> viewedNewsIds = newsHistoryService.findViewedNewsIds(chatId);
        List<NewsArticleDto> newArticles = response.articles().stream()
                .filter(article -> article.title() != null && article.url() != null)
                .filter(article -> !viewedNewsIds.contains(newsHistoryService.articleId(article)))
                .limit(RESPONSE_ARTICLES_LIMIT)
                .toList();

        if (newArticles.isEmpty()) {
            return scheduled
                    ? BotResponse.empty()
                    : BotResponse.of(BotCommand.sendMessage(chatId, "Новых новостей по вашей профессии пока нет."));
        }

        newsHistoryService.saveViewed(chatId, theme, newArticles);

        List<BotCommand> commands = new ArrayList<>();
        commands.add(BotCommand.sendMessage(
                chatId,
                (scheduled ? "🔔 Свежие новости" : "📰 Новости")
                + "\n\nТема: " + theme
                + "\nНайдено: " + newArticles.size() + " " + materialWord(newArticles.size())
        ));
        newArticles.stream()
                .map(article -> BotCommand.sendMessage(chatId, formatArticle(article)))
                .forEach(commands::add);

        return new BotResponse(commands);
    }

    private String formatArticle(NewsArticleDto article) {
        String source = article.source() == null || article.source().name() == null
                ? "Источник не указан"
                : article.source().name();
        String publishedAt = article.publishedAt() == null
                ? "Дата не указана"
                : article.publishedAt().format(DATE_FORMATTER);
        String description = article.description() == null || article.description().isBlank()
                ? ""
                : "\n\n" + trimDescription(article.description());

        return "🗞 " + article.title()
                + "\n\n📍 Источник: " + source
                + "\n🕒 Опубликовано: " + publishedAt
                + description
                + "\n\n🔗 Читать: " + article.url();
    }

    private String trimDescription(String description) {
        String normalized = description.trim();
        if (normalized.length() <= 300) {
            return normalized;
        }
        return normalized.substring(0, 297).trim() + "...";
    }

    private String materialWord(int count) {
        return count == 1 ? "материал" : "материала";
    }
}
