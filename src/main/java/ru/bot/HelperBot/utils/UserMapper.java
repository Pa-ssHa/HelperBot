package ru.bot.HelperBot.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
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
        users.setChat_id(userSession.getChat_id());
        users.setProfession(userSession.getNameVacancy());
        return users;
    }
}
