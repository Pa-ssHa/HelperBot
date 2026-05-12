package ru.bot.user_vacancy.user.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.bot.user_vacancy.user.domain.UserSession;
import ru.bot.user_vacancy.user.domain.UserState;

import java.time.LocalDate;

@Service
public class RedisSessionService {

    private final RedisTemplate<String, UserSession> redisTemplate;

    @Autowired
    public RedisSessionService(RedisTemplate<String, UserSession> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String key(Long chatId){
        return "session: " + chatId;
    }

    public UserSession getOrCreate(Long chatId){
        UserSession userSession = redisTemplate.opsForValue().get(key(chatId));
        if(userSession == null){
            userSession = new UserSession();
            userSession.setChatId(chatId);
            userSession.setUserState(UserState.START);
            userSession.setDate(LocalDate.now());
            redisTemplate.opsForValue().set(key(chatId), userSession);
        }
        return userSession;
    }

    public void save(Long chatId, UserSession userSession){
        redisTemplate.opsForValue().set(key(chatId), userSession);
    }

    public void delete(Long chatId){
        redisTemplate.delete(key(chatId));
    }
}
