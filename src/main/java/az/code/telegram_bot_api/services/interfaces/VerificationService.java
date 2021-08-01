package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.*;
import az.code.telegram_bot_api.models.DTOs.LoginDTO;
import az.code.telegram_bot_api.models.DTOs.ResetPasswordDTO;
import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.models.enums.TokenType;
import org.springframework.http.HttpStatus;

import javax.mail.MessagingException;
import java.io.IOException;

public interface VerificationService {
    void sendVerifyToken(User user, String url) throws MessagingException, IOException;

    VerificationToken findByToken(String token, TokenType tokenType);

    HttpStatus passwordForgot(LoginDTO loginDTO, String url) throws MessagingException, IOException;

    HttpStatus verify(String token);

    HttpStatus resetWithToken(String token, ResetPasswordDTO resetPasswordDTO);

    HttpStatus resetWithOldPassword(UserTokenDTO user, ResetPasswordDTO resetPasswordDTO);
}
