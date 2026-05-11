package ru.bot.HelperBot.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", schema = "helperbot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @Column(name = "chat_id")
    private Long chatId;
    private String lastName;
    private String firstName;
    private int age;
    private String city;
    private String profession;
}
