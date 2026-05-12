package ru.bot.user_vacancy.vacancy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bot.user_vacancy.vacancy.domain.Vacancy;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}
