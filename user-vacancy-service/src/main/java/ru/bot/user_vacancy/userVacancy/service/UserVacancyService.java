package ru.bot.user_vacancy.userVacancy.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bot.user_vacancy.vacancy.domain.Vacancy;
import ru.bot.user_vacancy.userVacancy.domain.UserVacancy;
import ru.bot.user_vacancy.userVacancy.domain.UserVacancyId;
import ru.bot.user_vacancy.userVacancy.repository.UserVacancyRepository;

import java.util.ArrayList;
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
            userVacancy.setUserId(chatId);
            userVacancy.setVacancyId(v.getIdVacancy());
            userVacancyRepository.save(userVacancy);
        }
    }

    @Transactional
    public void makeFavoriteVacancy(Long chatId, Long vacancyId){
        UserVacancyId userVacancyId = new UserVacancyId();
        userVacancyId.setUserId(chatId);
        userVacancyId.setVacancyId(vacancyId);
        Optional<UserVacancy> userVacancy = userVacancyRepository.findById(userVacancyId);
        userVacancy.ifPresent(vacancy -> {
            vacancy.setFavorite(true);
            userVacancyRepository.save(vacancy);
        });
    }

    @Transactional
    public void makeHiddenVacancy(Long chatId, Long vacancyId){
        UserVacancyId userVacancyId = new UserVacancyId();
        userVacancyId.setUserId(chatId);
        userVacancyId.setVacancyId(vacancyId);
        Optional<UserVacancy> userVacancy = userVacancyRepository.findById(userVacancyId);
        userVacancy.ifPresent(vacancy -> {
            vacancy.setHidden(true);
            userVacancyRepository.save(vacancy);
        });
    }

    public List<Long> findVacancyIdForUser(Long chatId){
        List<Long> result = new ArrayList<>();
        List<UserVacancy> userVacancyList = userVacancyRepository.findByUser_id(chatId);
        for(UserVacancy userVacancy : userVacancyList){
            result.add(userVacancy.getVacancyId());
        }
        return result;
    }

    public List<Long> findFavoriteVacancyIds(Long chatId) {
        return userVacancyRepository.findAllByUserIdAndIsFavoriteTrueAndIsHiddenFalse(chatId)
                .stream()
                .map(UserVacancy::getVacancyId)
                .toList();
    }
}
