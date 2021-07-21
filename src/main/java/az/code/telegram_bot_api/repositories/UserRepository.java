package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
}