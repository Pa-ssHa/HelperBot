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
import ru.bot.user_vacancy.user.service.UserService;
import ru.bot.user_vacancy.userVacancy.service.UserVacancyService;
import ru.bot.user_vacancy.vacancy.domain.Vacancy;
import ru.bot.user_vacancy.vacancy.repository.VacancyRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class VacancyService {

    private static final int HH_HTTP_TIMEOUT_MS = 5000;

    private final VacancyRepository vacancyRepository;
    private final UserVacancyService userVacancyService;
    private final UserService userService;
    private final RestClient restClient;


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

        List<Vacancy> vacancies = new ArrayList<>();

//        int numberPage = 0;
        final int[] numberPage = {0};

        int countVacancy = 5;
        int countAddedVacancies = 0;
        boolean flagCompete = false;

        List<Long> savedVacancies = userVacancyService.findVacancyIdForUser(chatId);

        while (true) {

            try {
                String response = requestVacancies(chatId, countVacancy, numberPage[0]);

                ObjectMapper mapper = new ObjectMapper();

                JsonNode jsonNode = mapper.readTree(response);
                int sizeResponse = jsonNode.get("items").size();

                if (sizeResponse == 0) {
                    break;
                }

                for (int i = 0; i < sizeResponse; i++) {

                    Vacancy vacancy = new Vacancy();
                    var item = jsonNode.get("items").get(i);

                    if (!savedVacancies.contains(Long.parseLong(item.get("id").asText()))) {
                        vacancy.setIdVacancy(Integer.parseInt(item.get("id").asText()));
                        vacancy.setNameVacancy(item.get("name").asText());
                        JsonNode salary = item.get("salary");

                        if (salary == null || salary.isNull()) {
                            vacancy.setSalary("Не указана");
                        } else {
                            vacancy.setSalary(salary.toString());
                        }
                        vacancy.setCity(item.get("area").get("name").asText());
                        vacancy.setCompany(item.get("employer").get("name").asText());
                        vacancies.add(vacancy);

//                snippet - требования
//                work_format - рабочий формат :[{id:..., name:...},{id, name}...]
//                experience :{id:..., name:...}
//                employment - занятость {id:..., name:...}
//                employer - компания {id:..., name:..., url...}


                        vacancyRepository.save(vacancy);
                        countAddedVacancies++;


                    }
                }
                if (sizeResponse < countVacancy) {
                    flagCompete = true;
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            if (countAddedVacancies == countVacancy || flagCompete) {
                break;
            } else {
                numberPage[0]++;
            }

        }

        userVacancyService.save(vacancies, chatId);

        return vacancies;
    }

    private SimpleClientHttpRequestFactory createRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(HH_HTTP_TIMEOUT_MS);
        requestFactory.setReadTimeout(HH_HTTP_TIMEOUT_MS);
        return requestFactory;
    }

    private String requestVacancies(Long chatId, int countVacancy, int page) {
        RestClientException lastException = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return restClient.get()
                        .uri(builder -> builder
                                .path("/vacancies")
                                .queryParam("per_page", countVacancy)
                                .queryParam("page", page)
                                .queryParam("text", userService.findByChatId(chatId).getProfession())
                                .build())
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
