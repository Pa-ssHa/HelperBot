package ru.bot.news_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bot.news_service.client.GatewayBotClient;
import ru.bot.news_service.domain.NewsSubscription;
import ru.bot.news_service.dto.BotResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsSubscriptionScheduler {

    private final NewsSubscriptionService newsSubscriptionService;
    private final NewsDeliveryService newsDeliveryService;
    private final GatewayBotClient gatewayBotClient;

    @Scheduled(cron = "${news.notifications.cron:0 0 9,20 * * *}", zone = "${news.notifications.zone:Europe/Moscow}")
    public void sendSubscribedNews() {
        for (NewsSubscription subscription : newsSubscriptionService.findActiveSubscriptions()) {
            try {
                BotResponse response = newsDeliveryService.buildNewsResponse(
                        subscription.getChatId(),
                        subscription.getTheme(),
                        true
                );
                gatewayBotClient.send(response);
            } catch (Exception e) {
                log.warn("Unable to send news subscription for chatId={}", subscription.getChatId(), e);
            }
        }
    }
}
