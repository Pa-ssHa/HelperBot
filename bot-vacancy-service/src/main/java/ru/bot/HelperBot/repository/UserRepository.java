package ru.bot.HelperBot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bot.HelperBot.model.user.UserSession;
import ru.bot.HelperBot.model.user.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByChatId(Long chatId);
}
