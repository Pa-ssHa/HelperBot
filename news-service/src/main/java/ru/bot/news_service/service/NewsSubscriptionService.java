package ru.bot.news_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bot.news_service.domain.NewsSubscription;
import ru.bot.news_service.repository.NewsSubscriptionRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsSubscriptionService {

    private final NewsSubscriptionRepository repository;

    @Transactional
    public void subscribe(Long chatId, String theme) {
        NewsSubscription subscription = repository.findById(chatId)
                .orElseGet(NewsSubscription::new);

        subscription.setChatId(chatId);
        subscription.setTheme(theme);
        subscription.setActive(true);
        if (subscription.getCreatedAt() == null) {
            subscription.setCreatedAt(OffsetDateTime.now());
        }
        subscription.setUpdatedAt(OffsetDateTime.now());

        repository.save(subscription);
    }

    @Transactional
    public void unsubscribe(Long chatId) {
        repository.findById(chatId).ifPresent(subscription -> {
            subscription.setActive(false);
            subscription.setUpdatedAt(OffsetDateTime.now());
            repository.save(subscription);
        });
    }

    public List<NewsSubscription> findActiveSubscriptions() {
        return repository.findAllByActiveTrue();
    }
}
