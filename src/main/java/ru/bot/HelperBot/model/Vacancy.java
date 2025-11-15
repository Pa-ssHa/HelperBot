package ru.bot.HelperBot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vacancy", schema = "helperbot")
public class Vacancy {

    @Id
    private long id_vacancy;
    private String nameVacancy;
    private String salary;    // сделать: "от-до валюта"
    private String city;
    private String company;

    public long getId_vacancy() {
        return id_vacancy;
    }

    public void setId_vacancy(long id_vacancy) {
        this.id_vacancy = id_vacancy;
    }

    public String getNameVacancy() {
        return nameVacancy;
    }

    public void setNameVacancy(String nameVacancy) {
        this.nameVacancy = nameVacancy;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
                "id_vacancy=" + id_vacancy +
                ", nameVacancy='" + nameVacancy + '\'' +
                ", salary='" + salary + '\'' +
                ", city='" + city + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
