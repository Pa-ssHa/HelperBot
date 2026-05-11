package ru.bot.HelperBot.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    private Long chatId;
    private UserState userState;
    private int age;
    private String firstName;
    private String lastName;
    private String city;
    private LocalDate date;
    private String nameVacancy;
    private LocalDateTime lastActivity;

    @Override
    public String toString() {
        return "Имя: " + firstName +
                ", Фамилия: " + lastName +
                ", Возраст: " + age +
                ", Город: " + city +
                ", Название вакансии: " + nameVacancy;
    }
}
