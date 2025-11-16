package ru.bot.HelperBot.utils;

import org.springframework.stereotype.Component;
import ru.bot.HelperBot.model.user.UserSession;
import ru.bot.HelperBot.model.user.Users;

@Component
public class UserMapper {

    public Users toUsers(UserSession userSession){
        Users users = new Users();
        users.setFirstName(userSession.getFirstName());
        users.setLastName(userSession.getLastName());
        users.setAge(userSession.getAge());
        users.setCity(userSession.getCity());
        users.setChatId(userSession.getChat_id());
        users.setProfession(userSession.getNameVacancy());
        return users;
    }
}
