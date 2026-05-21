package ru.bot.news_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_news", schema = "helperbot")
@IdClass(UserNewsId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserNews {

    @Id
    private Long userId;

    @Id
    private String newsId;

    private boolean isViewed = true;
    private OffsetDateTime viewedAt;
}
