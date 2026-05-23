package ru.bot.HelperBot.userVacancy.message;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageInfoService {

    public List<List<String>> mainMenu() {
        return List.of(
                List.of("👤 Моя анкета", "🔎 Найти вакансии"),
                List.of("📰 Новости", "🔔 Подписка"),
                List.of("📄 Анализ резюме")
        );
    }
}
