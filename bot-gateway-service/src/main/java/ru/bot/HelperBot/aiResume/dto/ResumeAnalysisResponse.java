package ru.bot.HelperBot.aiResume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysisResponse {
    private int score;
    private int profession_match;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> recommendations;
}