package ru.bot.user_vacancy.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bot.user_vacancy.user.domain.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByChatId(Long chatId);
}
