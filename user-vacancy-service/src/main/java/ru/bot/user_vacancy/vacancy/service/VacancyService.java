package ru.bot.user_vacancy.vacancy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.bot.user_vacancy.user.domain.Users;
import ru.bot.user_vacancy.user.service.UserService;
import ru.bot.user_vacancy.userVacancy.service.UserVacancyService;
import ru.bot.user_vacancy.vacancy.domain.Vacancy;
import ru.bot.user_vacancy.vacancy.repository.VacancyRepository;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VacancyService {

    private static final int HH_HTTP_TIMEOUT_MS = 5000;
    private static final int MANUAL_SEARCH_LIMIT = 5;
    private static final int RECENT_SEARCH_PAGE_SIZE = 20;
    private static final int RECENT_SEARCH_MAX_PAGES = 3;

    private final VacancyRepository vacancyRepository;
    private final UserVacancyService userVacancyService;
    private final UserService userService;
    private final RestClient restClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public VacancyService(
            VacancyRepository vacancyRepository,
            UserVacancyService userVacancyService,
            UserService userService,
            @Value("${hh.access.token}") String hhAccessToken
    ) {
        this.vacancyRepository = vacancyRepository;
        this.userVacancyService = userVacancyService;
        this.userService = userService;

        RestClient.Builder builder = RestClient.builder()
                .baseUrl("https://api.hh.ru")
                .requestFactory(createRequestFactory())
                .defaultHeader("User-Agent", "Mozilla/5.0 HelperBot/1.0 (pogoreloffpaulus@yandex.ru)")
                .defaultHeader("HH-User-Agent", "HelperBot/1.0 (pogoreloffpaulus@yandex.ru)")
                .defaultHeader("Accept", "application/json")
                .defaultHeader(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU,ru;q=0.9,en;q=0.8");

        if (hhAccessToken != null && !hhAccessToken.isBlank()) {
            builder.defaultHeader("Authorization", "Bearer " + hhAccessToken);
        }

        this.restClient = builder.build();
    }

    public List<Vacancy> findVacancy(Long chatId) {
        Users user = userService.findByChatId(chatId);
        List<Vacancy> vacancies = collectVacancies(user, null, MANUAL_SEARCH_LIMIT, MANUAL_SEARCH_LIMIT, Integer.MAX_VALUE);
        userVacancyService.save(vacancies, chatId);
        return vacancies;
    }

    public List<Vacancy> findRecentVacanciesForUser(Users user, OffsetDateTime publishedAfter, int limit) {
        return collectVacancies(user, publishedAfter, limit, RECENT_SEARCH_PAGE_SIZE, RECENT_SEARCH_MAX_PAGES);
    }

    public void markVacanciesAsSeen(Long chatId, List<Vacancy> vacancies) {
        userVacancyService.save(vacancies, chatId);
    }

    private List<Vacancy> collectVacancies(
            Users user,
            OffsetDateTime publishedAfter,
            int limit,
            int pageSize,
            int maxPages
    ) {
        List<Vacancy> vacancies = new ArrayList<>();
        if (user == null || user.getProfession() == null || user.getProfession().isBlank()) {
            return vacancies;
        }

        List<Long> savedVacancies = userVacancyService.findVacancyIdForUser(user.getChatId());
        int page = 0;

        while (vacancies.size() < limit && page < maxPages) {
            try {
                String response = requestVacancies(user.getProfession(), pageSize, page, publishedAfter);
                JsonNode items = mapper.readTree(response).get("items");

                if (items == null || items.isEmpty()) {
                    break;
                }

                for (JsonNode item : items) {
                    Vacancy vacancy = toVacancy(item);
                    if (savedVacancies.contains(vacancy.getIdVacancy())) {
                        continue;
                    }
                    if (publishedAfter != null && (vacancy.getPublishedAt() == null || vacancy.getPublishedAt().isBefore(publishedAfter))) {
                        continue;
                    }

                    vacancyRepository.save(vacancy);
                    vacancies.add(vacancy);
                    savedVacancies.add(vacancy.getIdVacancy());

                    if (vacancies.size() == limit) {
                        break;
                    }
                }

                if (items.size() < pageSize) {
                    break;
                }
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Unable to parse HH vacancies response", e);
            }
            page++;
        }

        return vacancies;
    }

    private Vacancy toVacancy(JsonNode item) {
        Vacancy vacancy = new Vacancy();
        vacancy.setIdVacancy(item.path("id").asLong());
        vacancy.setNameVacancy(item.path("name").asText());
        vacancy.setSalary(formatSalary(item.get("salary")));
        vacancy.setCity(item.path("area").path("name").asText());
        vacancy.setCompany(item.path("employer").path("name").asText());
        vacancy.setPublishedAt(parsePublishedAt(item.get("published_at")));
        return vacancy;
    }

    private String formatSalary(JsonNode salary) {
        if (salary == null || salary.isNull()) {
            return "Не указана";
        }

        Integer from = salary.hasNonNull("from") ? salary.get("from").asInt() : null;
        Integer to = salary.hasNonNull("to") ? salary.get("to").asInt() : null;
        String currency = formatCurrency(salary.path("currency").asText(""));

        String amount;
        if (from != null && to != null) {
            amount = "от " + formatMoney(from) + " до " + formatMoney(to);
        } else if (from != null) {
            amount = "от " + formatMoney(from);
        } else if (to != null) {
            amount = "до " + formatMoney(to);
        } else {
            amount = "Не указана";
        }

        if ("Не указана".equals(amount)) {
            return amount;
        }

        String tax = salary.hasNonNull("gross") && salary.get("gross").asBoolean()
                ? " до вычета налогов"
                : " на руки";
        return amount + " " + currency + tax;
    }

    private String formatCurrency(String currency) {
        return switch (currency) {
            case "RUR", "RUB" -> "руб.";
            case "USD" -> "USD";
            case "EUR" -> "EUR";
            default -> currency == null || currency.isBlank() ? "" : currency;
        };
    }

    private String formatMoney(Integer value) {
        return String.format("%,d", value).replace(',', ' ');
    }

    private OffsetDateTime parsePublishedAt(JsonNode publishedAt) {
        if (publishedAt == null || publishedAt.isNull()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(publishedAt.asText());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private SimpleClientHttpRequestFactory createRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(HH_HTTP_TIMEOUT_MS);
        requestFactory.setReadTimeout(HH_HTTP_TIMEOUT_MS);
        return requestFactory;
    }

    private String requestVacancies(String profession, int countVacancy, int page, OffsetDateTime publishedAfter) {
        RestClientException lastException = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return restClient.get()
                        .uri(builder -> {
                            builder.path("/vacancies")
                                    .queryParam("per_page", countVacancy)
                                    .queryParam("page", page)
                                    .queryParam("text", profession);
                            if (publishedAfter != null) {
                                builder.queryParam("date_from", publishedAfter)
                                        .queryParam("order_by", "publication_time");
                            }
                            return builder.build();
                        })
                        .retrieve()
                        .body(String.class);
            } catch (RestClientException e) {
                lastException = e;
                if (attempt == 3) {
                    throw e;
                }
                try {
                    Thread.sleep(300L * attempt);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw e;
                }
            }
        }
        throw lastException;
    }
}
