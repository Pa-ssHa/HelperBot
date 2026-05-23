package ru.bot.news_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bot.news_service.client.UserDetailsClient;
import ru.bot.news_service.client.GatewayBotClient;
import ru.bot.news_service.domain.NewsSubscription;
import ru.bot.news_service.dto.BotResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsSubscriptionScheduler {

    private final NewsSubscriptionService newsSubscriptionService;
    private final NewsDeliveryService newsDeliveryService;
    private final UserDetailsClient userDetailsClient;
    private final GatewayBotClient gatewayBotClient;

    @Scheduled(cron = "${news.notifications.cron:0 0 9,20 * * *}", zone = "${news.notifications.zone:Europe/Moscow}")
    public void sendSubscribedNews() {
        for (NewsSubscription subscription : newsSubscriptionService.findActiveSubscriptions()) {
            try {
                String theme = userDetailsClient.findNewsTheme(subscription.getChatId());
                if (theme == null || theme.isBlank()) {
                    log.info("Skip news subscription without actual theme for chatId={}", subscription.getChatId());
                    continue;
                }

                if (!theme.equals(subscription.getTheme())) {
                    newsSubscriptionService.subscribe(subscription.getChatId(), theme);
                }

                BotResponse response = newsDeliveryService.buildNewsResponse(
                        subscription.getChatId(),
                        theme,
                        true
                );
                gatewayBotClient.send(response);
            } catch (Exception e) {
                log.warn("Unable to send news subscription for chatId={}", subscription.getChatId(), e);
            }
        }
    }
}
