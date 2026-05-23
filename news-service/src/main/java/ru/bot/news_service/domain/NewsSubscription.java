package ru.bot.news_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "news_subscription", schema = "helperbot")
@Getter
@Setter
@NoArgsConstructor
public class NewsSubscription {

    @Id
    private Long chatId;

    @Column(nullable = false)
    private String theme;

    @Column(nullable = false)
    private boolean active = true;


    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
