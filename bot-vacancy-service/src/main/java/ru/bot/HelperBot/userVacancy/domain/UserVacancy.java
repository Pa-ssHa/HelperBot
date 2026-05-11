package ru.bot.HelperBot.userVacancy.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_vacancy", schema = "helperbot")
@IdClass(UserVacancyId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserVacancy {

    @Id
    private long userId;

    @Id
    private long vacancyId;
    private boolean isFavorite;
    private boolean isHidden;
}
