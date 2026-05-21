package ru.bot.user_vacancy.vacancy.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

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
    private String salary;
    private String city;
    private String company;
    private OffsetDateTime publishedAt;

    @Override
    public String toString() {
        return "Vacancy{" +
               "idVacancy=" + idVacancy +
               ", nameVacancy='" + nameVacancy + '\'' +
               ", salary='" + salary + '\'' +
               ", city='" + city + '\'' +
               ", company='" + company + '\'' +
               ", publishedAt=" + publishedAt +
               '}';
    }
}
