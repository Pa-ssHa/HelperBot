package ru.bot.user_vacancy.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bot.user_vacancy.user.domain.Users;
import ru.bot.user_vacancy.user.service.UserService;

@RestController
@RequestMapping("internal/v1/user")
public class UserDataRestController {

    private final UserService userService;

    public UserDataRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/news-theme/{id}")
    public String getUserNewsTheme(@PathVariable("id") Long chatId){
        Users user = userService.findByChatId(chatId);
        if(user == null){
            return null;
        }
        return user.getProfession();
    }
}
