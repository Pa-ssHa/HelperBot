package ru.bot.HelperBot.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_vacancy", schema = "helperbot")
@IdClass(UserVacancyId.class)
public class UserVacancy {

    @Id
    private long user_id;

    @Id
    private long vacancy_id;
    private boolean is_favorite;
    private boolean is_hidden;

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

    public boolean isIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public boolean isIs_hidden() {
        return is_hidden;
    }

    public void setIs_hidden(boolean is_hidden) {
        this.is_hidden = is_hidden;
    }
}
