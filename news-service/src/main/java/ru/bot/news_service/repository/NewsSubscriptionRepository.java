package ru.bot.news_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bot.news_service.domain.NewsSubscription;

import java.util.List;

@Repository
public interface NewsSubscriptionRepository extends JpaRepository<NewsSubscription, Long> {
    List<NewsSubscription> findAllByActiveTrue();
}
