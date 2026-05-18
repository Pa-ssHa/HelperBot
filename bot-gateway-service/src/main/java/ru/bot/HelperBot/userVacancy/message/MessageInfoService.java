package ru.bot.HelperBot.userVacancy.message;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageInfoService {

    public List<List<String>> mainMenu() {
        return List.of(
                List.of("/my_info", "/sub_news"),
                List.of("/searchVacancy", "/analyse")
        );
    }
}
