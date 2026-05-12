package ru.bot.user_vacancy.userVacancy.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class UserVacancyId implements Serializable {
    private long userId;
    private long vacancyId;

    @Override
    public boolean equals(Object o) {
        if(this == o ) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        UserVacancyId userVacancyId = (UserVacancyId) o;
        return Objects.equals(userVacancyId.getUserId(), userId) &&
               Objects.equals(userVacancyId.getVacancyId(), vacancyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, vacancyId);
    }
}
