package ru.bot.HelperBot.vacancy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.bot.HelperBot.user.service.UserService;
import ru.bot.HelperBot.userVacancy.service.UserVacancyService;
import ru.bot.HelperBot.vacancy.domain.Vacancy;
import ru.bot.HelperBot.vacancy.repository.VacancyRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final UserVacancyService userVacancyService;
    private final UserService userService;

    private final String URI = "https://api.hh.ru/vacancies";
    private final RestClient restClient = RestClient.create();

    @Autowired
    public VacancyService(VacancyRepository vacancyRepository, UserVacancyService userVacancyService, UserService userService) {
        this.vacancyRepository = vacancyRepository;
        this.userVacancyService = userVacancyService;
        this.userService = userService;
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
                String response = restClient.get()
                        .uri(URI, builder -> builder
                                .queryParam("per_page", countVacancy)
                                .queryParam("page", numberPage[0])
                                .queryParam("text", userService.findByChatId(chatId).getProfession())
                                .build())
                        .retrieve()
                        .body(String.class);

                ObjectMapper mapper = new ObjectMapper();

                JsonNode jsonNode = mapper.readTree(response);
                int sizeResponse = jsonNode.get("items").size();

                if (sizeResponse == 0 ) {
                    break;
                }

                for (int i = 0; i < sizeResponse; i++) {

                    Vacancy vacancy = new Vacancy();
                    var item = jsonNode.get("items").get(i);

                    if (!savedVacancies.contains(Long.parseLong(item.get("id").asText()))) {
                        vacancy.setIdVacancy(Integer.parseInt(item.get("id").asText()));
                        vacancy.setNameVacancy(item.get("name").asText());
                        vacancy.setSalary(item.get("salary_range").asText());
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
                if (sizeResponse < countVacancy){
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


}
