package ru.bot.HelperBot.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bot.HelperBot.model.Vacancy;
import ru.bot.HelperBot.model.user.UserVacancy;
import ru.bot.HelperBot.model.user.UserVacancyId;
import ru.bot.HelperBot.repository.UserVacancyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserVacancyService {

    private final UserVacancyRepository userVacancyRepository;

    @Autowired
    public UserVacancyService(UserVacancyRepository userVacancyRepository) {
        this.userVacancyRepository = userVacancyRepository;
    }

    public void save(List<Vacancy> vacancies, Long chatId){
        for(Vacancy v : vacancies){
            UserVacancy userVacancy = new UserVacancy();
            userVacancy.setUser_id(chatId);
            userVacancy.setVacancy_id(v.getId_vacancy());
            userVacancyRepository.save(userVacancy);
        }
    }

    @Transactional
    public void makeFavoriteVacancy(Long chatId, Long vacancyId){
        UserVacancyId userVacancyId = new UserVacancyId();
        userVacancyId.setUser_id(chatId);
        userVacancyId.setVacancy_id(vacancyId);
        Optional<UserVacancy> userVacancy = userVacancyRepository.findById(userVacancyId);
        userVacancy.ifPresent(vacancy -> {
            vacancy.setIs_favorite(true);
            userVacancyRepository.save(vacancy);
        });
    }

    @Transactional
    public void makeHiddenVacancy(Long chatId, Long vacancyId){
        UserVacancyId userVacancyId = new UserVacancyId();
        userVacancyId.setUser_id(chatId);
        userVacancyId.setVacancy_id(vacancyId);
        Optional<UserVacancy> userVacancy = userVacancyRepository.findById(userVacancyId);
        userVacancy.ifPresent(vacancy -> {
            vacancy.setIs_hidden(true);
            userVacancyRepository.save(vacancy);
        });
    }
}
