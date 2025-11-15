package ru.bot.HelperBot.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

public class UserVacancyId implements Serializable {
    private long user_id;
    private long vacancy_id;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getVacancy_id() {
        return vacancy_id;
    }

    public void setVacancy_id(long vacancy_id) {
        this.vacancy_id = vacancy_id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o ) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        UserVacancyId userVacancyId = (UserVacancyId) o;
        return Objects.equals(userVacancyId.getUser_id(), user_id) &&
                Objects.equals(userVacancyId.getVacancy_id(), vacancy_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, vacancy_id);
    }
}
