package ru.bot.user_vacancy.vacancy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import ru.bot.user_vacancy.bot.client.GatewayBotClient;
import ru.bot.user_vacancy.common.dto.BotCommand;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.user.domain.Users;
import ru.bot.user_vacancy.user.repository.UserRepository;
import ru.bot.user_vacancy.vacancy.domain.Vacancy;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewVacancyNotificationScheduler {

    private static final int NOTIFICATION_LIMIT = 2;

    private final UserRepository userRepository;
    private final VacancyService vacancyService;
    private final VacancySearchMessageService vacancySearchMessageService;
    private final GatewayBotClient gatewayBotClient;

    @Value("${vacancy.notifications.lookback-minutes:3}")
    private long lookbackMinutes;

    @Scheduled(
            fixedDelayString = "${vacancy.notifications.delay-ms:180000}",
            initialDelayString = "${vacancy.notifications.initial-delay-ms:60000}"
    )
    public void checkNewVacancies() {
        log.info("Scheduled vacancy");
        OffsetDateTime publishedAfter = OffsetDateTime.now().minusMinutes(lookbackMinutes);

        for (Users user : userRepository.findAll()) {
            if (user.getProfession() == null || user.getProfession().isBlank()) {
                continue;
            }

            log.info("Scheduled vacancy for user with {}", user.getChatId());


            try {
                notifyUserAboutRecentVacancies(user, publishedAfter);
            } catch (RestClientException e) {
                log.warn("HH request failed for chatId={}", user.getChatId(), e);
            } catch (Exception e) {
                log.warn("Unable to notify chatId={} about recent vacancies", user.getChatId(), e);
            }
        }
    }

    private void notifyUserAboutRecentVacancies(Users user, OffsetDateTime publishedAfter) {
        List<Vacancy> recentVacancies = vacancyService.findRecentVacanciesForUser(
                user,
                publishedAfter,
                NOTIFICATION_LIMIT
        );

        if (recentVacancies.isEmpty()) {
            return;
        }

        List<BotCommand> commands = new ArrayList<>();
        commands.add(BotCommand.sendMessage(
                user.getChatId(),
                "Появились свежие вакансии по вашей профессии: " + user.getProfession()
        ));
        recentVacancies.stream()
                .map(vacancy -> vacancySearchMessageService.toCommand(user.getChatId(), vacancy))
                .forEach(commands::add);

        boolean sent = gatewayBotClient.send(new BotResponse(commands));
        if (sent) {
            vacancyService.markVacanciesAsSeen(user.getChatId(), recentVacancies);
        }
    }
}
