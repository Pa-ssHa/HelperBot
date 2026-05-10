package ru.bot.ai_resume.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResumeAnalysisResponse {
    private int score;
    private int profession_match;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> recommendations;
}
