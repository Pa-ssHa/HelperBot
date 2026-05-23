package ru.bot.user_vacancy.vacancy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bot.user_vacancy.vacancy.domain.Vacancy;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findAllByIdVacancyIn(List<Long> ids);
}
