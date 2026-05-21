package ru.bot.news_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bot.news_service.domain.NewsArticle;
import ru.bot.news_service.domain.UserNews;
import ru.bot.news_service.dto.NewsArticleDto;
import ru.bot.news_service.repository.NewsArticleRepository;
import ru.bot.news_service.repository.UserNewsRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NewsHistoryService {

    private final NewsArticleRepository newsArticleRepository;
    private final UserNewsRepository userNewsRepository;

    public Set<String> findViewedNewsIds(Long chatId) {
        return Set.copyOf(userNewsRepository.findViewedNewsIds(chatId));
    }

    public String articleId(NewsArticleDto article) {
        return sha256(article.url());
    }

    @Transactional
    public void saveViewed(Long chatId, String theme, List<NewsArticleDto> articles) {
        for (NewsArticleDto article : articles) {
            String newsId = articleId(article);
            newsArticleRepository.save(toEntity(newsId, theme, article));

            UserNews userNews = new UserNews();
            userNews.setUserId(chatId);
            userNews.setNewsId(newsId);
            userNews.setViewed(true);
            userNews.setViewedAt(OffsetDateTime.now());
            userNewsRepository.save(userNews);
        }
    }

    private NewsArticle toEntity(String id, String theme, NewsArticleDto article) {
        NewsArticle newsArticle = new NewsArticle();
        newsArticle.setId(id);
        newsArticle.setTitle(article.title());
        newsArticle.setDescription(article.description());
        newsArticle.setUrl(article.url());
        newsArticle.setSourceName(article.source() == null ? null : article.source().name());
        newsArticle.setTheme(theme);
        newsArticle.setPublishedAt(article.publishedAt());
        newsArticle.setCreatedAt(OffsetDateTime.now());
        return newsArticle;
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
