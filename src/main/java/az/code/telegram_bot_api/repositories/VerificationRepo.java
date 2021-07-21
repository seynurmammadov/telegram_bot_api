package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.VerificationToken;
import az.code.telegram_bot_api.models.enums.TokenType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationRepo extends CrudRepository<VerificationToken, String> {
    @Query("select t from VerificationToken t where t.token=:token and t.tokenType=:tokenType")
    Optional<VerificationToken> findByToken(String token, TokenType tokenType);
}
