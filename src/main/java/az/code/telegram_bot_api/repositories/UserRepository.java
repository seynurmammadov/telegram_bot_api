package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query("select u FROM User u where u.isActive=TRUE")
    List<User> getAllActive();
}