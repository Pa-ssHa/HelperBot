package ru.bot.HelperBot.user.facade;

import org.springframework.stereotype.Component;
import ru.bot.HelperBot.user.domain.UserSession;
import ru.bot.HelperBot.user.domain.Users;

@Component
public class UserMapper {

    public Users toUsers(UserSession userSession){
        Users users = new Users();
        users.setFirstName(userSession.getFirstName());
        users.setLastName(userSession.getLastName());
        users.setAge(userSession.getAge());
        users.setCity(userSession.getCity());
        users.setChatId(userSession.getChatId());
        users.setProfession(userSession.getNameVacancy());
        return users;
    }
}
