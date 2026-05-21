package ru.bot.news_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bot.news_service.domain.NewsArticle;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, String> {
}
