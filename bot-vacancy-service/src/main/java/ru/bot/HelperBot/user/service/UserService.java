package ru.bot.HelperBot.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bot.HelperBot.user.facade.UserMapper;
import ru.bot.HelperBot.user.domain.UserSession;
import ru.bot.HelperBot.user.domain.Users;
import ru.bot.HelperBot.user.repository.UserRepository;

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
