package ru.bot.user_vacancy.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bot.user_vacancy.user.facade.UserMapper;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.domain.Users;
import ru.bot.user_vacancy.user.repository.UserRepository;

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

    public Users findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }


}
