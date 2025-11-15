package ru.bot.HelperBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bot.HelperBot.model.user.UserVacancy;
import ru.bot.HelperBot.model.user.UserVacancyId;

@Repository
public interface UserVacancyRepository extends JpaRepository<UserVacancy, UserVacancyId> {
}
