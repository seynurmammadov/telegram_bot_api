package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.VerificationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationRepo extends CrudRepository<VerificationToken, String> {
    Optional<VerificationToken> findByToken(String token);
}
