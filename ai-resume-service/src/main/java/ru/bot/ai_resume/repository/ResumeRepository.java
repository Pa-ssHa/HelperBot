package ru.bot.ai_resume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bot.ai_resume.domain.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
