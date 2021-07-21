package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.LoginDTO;
import az.code.telegram_bot_api.models.ResetPasswordDTO;
import az.code.telegram_bot_api.models.User;
import az.code.telegram_bot_api.models.VerificationToken;
import az.code.telegram_bot_api.models.enums.TokenType;
import org.springframework.http.HttpStatus;

public interface VerificationService {
    void sendVerifyToken(User user);
    VerificationToken findByToken(String token, TokenType tokenType);
    HttpStatus passwordForgot(LoginDTO loginDTO) ;
    HttpStatus verify(String token);
    HttpStatus resetPassword(String token, ResetPasswordDTO resetPasswordDTO);
}
