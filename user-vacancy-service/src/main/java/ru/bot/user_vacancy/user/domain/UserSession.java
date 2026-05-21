package ru.bot.user_vacancy.user.domain;

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
    private Integer desiredSalary;
    private LocalDateTime lastActivity;

    @Override
    public String toString() {
        String salary = desiredSalary == null || desiredSalary <= 0
                ? "не указаны"
                : formatSalary(desiredSalary);

        return "Анкета сохранена\n\n" +
                "Имя: " + firstName + "\n" +
                "Фамилия: " + lastName + "\n" +
                "Возраст: " + age + "\n" +
                "Профессия: " + nameVacancy + "\n" +
                "Город: " + city + "\n" +
                "Зарплатные ожидания: " + salary;
    }

    private String formatSalary(Integer value) {
        return String.format("%,d", value).replace(',', ' ') + " руб.";
    }
}
