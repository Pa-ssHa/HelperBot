package ru.bot.user_vacancy.user.dispatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bot.user_vacancy.common.dto.BotResponse;
import ru.bot.user_vacancy.common.dto.BotCommand;
import ru.bot.user_vacancy.common.dto.UserMessageRequest;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.domain.UserState;
import ru.bot.user_vacancy.user.handler.PersonStateHandler;
import ru.bot.user_vacancy.user.redis.RedisSessionService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonFormDispatcher {

    private final List<PersonStateHandler> personHandlers;
    private final RedisSessionService redisSessionService;

    public BotResponse dispatch(UserMessageRequest request) {
        if (request.text() == null || request.text().isBlank()) {
            return BotResponse.empty();
        }

        Long chatId = request.chatId();
        log.info("chatId: {}", chatId);

        UserSession userSession = redisSessionService.getOrCreate(chatId);

        log.info("userSession: {}", userSession);

        if (isFillingForm(userSession) && isCommandInput(request.text())) {
            return BotResponse.of(BotCommand.sendMessage(
                    request.chatId(),
                    "Похоже, это команда, а не ответ для анкеты.\n\n" + promptFor(userSession.getUserState())
            ));
        }

        for (PersonStateHandler personHandler : personHandlers) {
            if (personHandler.canHandle(request, userSession)) {
                BotResponse response = personHandler.handle(request, userSession);
                redisSessionService.save(chatId, userSession);
                return response;
            }
        }

        return BotResponse.empty();
    }

    private boolean isFillingForm(UserSession userSession) {
        UserState state = userSession.getUserState();
        return state != null && state != UserState.START && state != UserState.FINISH;
    }

    private boolean isCommandInput(String text) {
        String value = text.trim();
        return value.startsWith("/")
               || "👤 Моя анкета".equalsIgnoreCase(value)
               || "🔎 Найти вакансии".equalsIgnoreCase(value)
               || "⭐ Избранное".equalsIgnoreCase(value)
               || "📰 Новости".equalsIgnoreCase(value)
               || "📰 Показать новости".equalsIgnoreCase(value)
               || "🔔 Подписка".equalsIgnoreCase(value)
               || "📰 Подписка на новости".equalsIgnoreCase(value)
               || "📄 Анализ резюме".equalsIgnoreCase(value);
    }

    private String promptFor(UserState state) {
        return switch (state) {
            case WAITING_FIRSTNAME -> "Введите имя.";
            case WAITING_LASTNAME -> "Введите фамилию.";
            case WAITING_AGE -> "Введите возраст числом, например 28.";
            case WAITING_VACANCY -> "Введите профессию или должность для поиска.";
            case WAITING_SALARY_EXPECTATION -> "Введите зарплатные ожидания числом, например 180000. Если не важно, отправьте 0.";
            case WAITING_CITY -> "Введите город для поиска работы.";
            case START, FINISH -> "";
        };
    }
}
