package ru.bot.HelperBot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.bot.HelperBot.model.Vacancy;
import ru.bot.HelperBot.model.user.Users;
import ru.bot.HelperBot.repository.UserVacancyRepository;
import ru.bot.HelperBot.repository.VacancyRepository;

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

    public List<Vacancy> findVacancy(Long chatId, int countVacancy) {

        List<Vacancy> vacancies = new ArrayList<>();

        String response = restClient.get()
                .uri(URI, builder -> builder
                        .queryParam("per_page", countVacancy)
                        .queryParam("text", userService.findByChatId(chatId).getProfession())
                        .build())
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(response);
            for (int i = 0; i < countVacancy; i++) {
                Vacancy vacancy = new Vacancy();
                var item = jsonNode.get("items").get(i);
                if(item==null){
                    return vacancies;
                }
                vacancy.setId_vacancy(Integer.parseInt(item.get("id").asText()));
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
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        userVacancyService.save(vacancies, chatId);

//        Лучше вернуть HashMap - id вакансии:описание

        return vacancies;
    }


}
