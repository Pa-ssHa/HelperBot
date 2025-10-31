package ru.bot.HelperBot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.bot.HelperBot.model.user.UserSession;
import ru.bot.HelperBot.model.user.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
}
