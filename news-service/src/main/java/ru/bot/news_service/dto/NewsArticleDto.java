package ru.bot.news_service.dto;

import java.time.OffsetDateTime;

public record NewsArticleDto(
        NewsSourceDto source,
        String author,
        String title,
        String description,
        String url,
        String urlToImage,
        OffsetDateTime publishedAt,
        String content
) {
}
