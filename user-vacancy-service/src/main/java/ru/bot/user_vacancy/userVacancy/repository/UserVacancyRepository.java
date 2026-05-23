package ru.bot.user_vacancy.userVacancy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bot.user_vacancy.userVacancy.domain.UserVacancy;
import ru.bot.user_vacancy.userVacancy.domain.UserVacancyId;

import java.util.List;

@Repository
public interface UserVacancyRepository extends JpaRepository<UserVacancy, UserVacancyId> {

    @Query("SELECT uv FROM UserVacancy uv WHERE uv.userId = ?1")
    List<UserVacancy> findByUser_id(Long chatId);

    List<UserVacancy> findAllByUserIdAndIsFavoriteTrueAndIsHiddenFalse(Long chatId);
}
