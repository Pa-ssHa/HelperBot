package ru.bot.news_service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bot.news_service.client.NewsApiClient;
import ru.bot.news_service.client.UserDetailsClient;
import ru.bot.news_service.dto.BotCommand;
import ru.bot.news_service.dto.BotResponse;
import ru.bot.news_service.dto.NewsApiResponse;
import ru.bot.news_service.dto.NewsArticleDto;
import ru.bot.news_service.dto.NewsMessageRequest;
import ru.bot.news_service.service.NewsHistoryService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SearchNewsMessageHandler implements NewsMessageHandler {

    private static final String NEWS_COMMAND = "/sub_news";
    private static final int RESPONSE_ARTICLES_LIMIT = 2;

    private final UserDetailsClient userDetailsClient;
    private final NewsApiClient newsApiClient;
    private final NewsHistoryService newsHistoryService;

    @Override
    public boolean canHandle(NewsMessageRequest request) {
        return request.text() != null && NEWS_COMMAND.equalsIgnoreCase(request.text().trim());
    }

    @Override
    public BotResponse handle(NewsMessageRequest request) {
        String theme = userDetailsClient.findNewsTheme(request.chatId());
        if (theme == null || theme.isBlank()) {
            return BotResponse.of(BotCommand.sendMessage(request.chatId(), "Сначала заполните профессию через /my_info"));
        }

        NewsApiResponse response = newsApiClient.search(theme);
        if (response == null) {
            return BotResponse.of(BotCommand.sendMessage(request.chatId(), "Не удалось получить новости. Попробуйте позже."));
        }

        if (!"ok".equalsIgnoreCase(response.status())) {
            String message = response.message() == null ? "News API вернул ошибку" : response.message();
            return BotResponse.of(BotCommand.sendMessage(request.chatId(), message));
        }

        if (response.articles() == null || response.articles().isEmpty()) {
            return BotResponse.of(BotCommand.sendMessage(request.chatId(), "По вашей профессии новости не найдены"));
        }

        Set<String> viewedNewsIds = newsHistoryService.findViewedNewsIds(request.chatId());
        List<NewsArticleDto> newArticles = response.articles().stream()
                .filter(article -> article.title() != null && article.url() != null)
                .filter(article -> !viewedNewsIds.contains(newsHistoryService.articleId(article)))
                .limit(RESPONSE_ARTICLES_LIMIT)
                .toList();

        if (newArticles.isEmpty()) {
            return BotResponse.of(BotCommand.sendMessage(request.chatId(), "Новых новостей по вашей профессии пока нет"));
        }

        newsHistoryService.saveViewed(request.chatId(), theme, newArticles);

        List<BotCommand> commands = new ArrayList<>();
        commands.add(BotCommand.sendMessage(request.chatId(), "Новости по теме: " + theme));
        newArticles.stream()
                .map(article -> BotCommand.sendMessage(request.chatId(), formatArticle(article)))
                .forEach(commands::add);

        return new BotResponse(commands);
    }

    private String formatArticle(NewsArticleDto article) {
        String source = article.source() == null || article.source().name() == null
                ? "Источник не указан"
                : article.source().name();
        String publishedAt = article.publishedAt() == null
                ? ""
                : "\n" + article.publishedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String description = article.description() == null || article.description().isBlank()
                ? ""
                : "\n\n" + article.description();

        return article.title()
                + "\n" + source
                + publishedAt
                + description
                + "\n\n" + article.url();
    }
}
