package ru.bot.user_vacancy.user.domain;

public enum UserState {
    START,
    WAITING_FIRSTNAME,
    WAITING_LASTNAME,
    WAITING_VACANCY,
    WAITING_SALARY_EXPECTATION,
    WAITING_AGE,
    FINISH,
    WAITING_CITY
}
