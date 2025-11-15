package ru.bot.HelperBot.model.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users", schema = "helperbot")
public class Users {

    @Id
    @Column(name = "chat_id")
    private Long chatId;
    private String lastName;
    private String firstName;
    private int age;
    private String city;
    private String profession;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chat_id) {
        this.chatId = chat_id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
