package ru.bot.HelperBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bot.HelperBot.model.user.UserVacancy;
import ru.bot.HelperBot.model.user.UserVacancyId;

import java.util.List;

@Repository
public interface UserVacancyRepository extends JpaRepository<UserVacancy, UserVacancyId> {

    @Query("SELECT uv FROM UserVacancy uv WHERE uv.user_id = ?1")
    List<UserVacancy> findByUser_id(Long chatId);
}
