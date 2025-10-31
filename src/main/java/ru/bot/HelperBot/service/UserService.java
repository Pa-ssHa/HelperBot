package ru.bot.HelperBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bot.HelperBot.utils.UserMapper;
import ru.bot.HelperBot.model.user.UserSession;
import ru.bot.HelperBot.model.user.Users;
import ru.bot.HelperBot.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void save(UserSession userSession) {
        userRepository.save(userMapper.toUsers(userSession));
    }


}
