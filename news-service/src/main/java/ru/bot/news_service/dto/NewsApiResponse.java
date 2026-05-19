package ru.bot.news_service.dto;

import java.util.List;

public record NewsApiResponse(
        String status,
        Integer totalResults,
        List<NewsArticleDto> articles,
        String code,
        String message
) {
}
