package ru.bot.news_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bot.news_service.domain.UserNews;
import ru.bot.news_service.domain.UserNewsId;

import java.util.List;

public interface UserNewsRepository extends JpaRepository<UserNews, UserNewsId> {

    @Query("select un.newsId from UserNews un where un.userId = ?1")
    List<String> findViewedNewsIds(Long userId);
}
