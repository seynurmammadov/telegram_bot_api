package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.VerificationToken;

public interface VerificationService {
    void sendVerifyToken(User user);
    VerificationToken findByToken(String token);
    void delete(VerificationToken token);
}
