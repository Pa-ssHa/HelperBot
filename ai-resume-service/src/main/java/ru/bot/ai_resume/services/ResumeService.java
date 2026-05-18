package ru.bot.ai_resume.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bot.ai_resume.domain.Resume;
import ru.bot.ai_resume.repository.ResumeRepository;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public Resume getResume(long id) {
        return resumeRepository.findById(id).orElse(null);
    }

    public Resume save(Resume resume) {
        return resumeRepository.save(resume);
    }
}
