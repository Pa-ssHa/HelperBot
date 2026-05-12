package ru.bot.user_vacancy.user.facade;

import org.springframework.stereotype.Component;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.domain.Users;

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
