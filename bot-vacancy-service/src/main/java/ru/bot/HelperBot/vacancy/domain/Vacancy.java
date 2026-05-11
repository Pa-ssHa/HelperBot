package ru.bot.HelperBot.vacancy.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vacancy", schema = "helperbot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {

    @Id
    private long idVacancy;
    private String nameVacancy;
    private String salary;    // сделать: "от-до валюта"
    private String city;
    private String company;

    @Override
    public String toString() {
        return "Vacancy{" +
               "id_vacancy=" + idVacancy +
               ", nameVacancy='" + nameVacancy + '\'' +
               ", salary='" + salary + '\'' +
               ", city='" + city + '\'' +
               ", company='" + company + '\'' +
               '}';
    }
}
