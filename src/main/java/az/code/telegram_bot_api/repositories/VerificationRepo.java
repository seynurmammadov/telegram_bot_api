package az.code.telegram_bot_api.repositories;

import az.code.telegram_bot_api.models.VerificationToken;
import az.code.telegram_bot_api.models.enums.TokenType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface VerificationRepo extends CrudRepository<VerificationToken, Long> {
    @Query("select t from VerificationToken t where t.token=:token and t.tokenType=:tokenType")
    Optional<VerificationToken> findByToken(String token, TokenType tokenType);
    @Modifying
    @Transactional
    @Query("delete from VerificationToken v where v=:token")
    void deleteToken(VerificationToken token);
}
