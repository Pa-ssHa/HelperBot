package ru.bot.ai_resume.facade;

import org.springframework.stereotype.Service;
import ru.bot.ai_resume.domain.Resume;
import ru.bot.ai_resume.dto.ResumeAnalysisResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResumeMapper {

    public Resume toEntity(
            ResumeAnalysisResponse response,
            Long userChatId,
            String fileName,
            String telegramFileId,
            String rawResponse
    ) {
        Resume entity = new Resume();

        entity.setUserChatId(userChatId);
        entity.setFileName(fileName);
        entity.setTelegramFileId(telegramFileId);

        entity.setScore(response.getScore());
        entity.setProfessionMatch(response.getProfession_match());

        entity.setStrengths(join(response.getStrengths()));
        entity.setWeaknesses(join(response.getWeaknesses()));
        entity.setRecommendations(join(response.getRecommendations()));

        entity.setRawResponse(rawResponse);
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    private String join(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }

        return values.stream()
                .map(value -> "• " + value)
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");
    }
}
