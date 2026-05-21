package ru.bot.user_vacancy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UserVacancyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserVacancyServiceApplication.class, args);
	}

}
