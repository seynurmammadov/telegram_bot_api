package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.DTOs.LoginDTO;
import az.code.telegram_bot_api.models.DTOs.TokenDTO;
import az.code.telegram_bot_api.models.DTOs.RegistrationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;

import javax.mail.MessagingException;
import java.io.IOException;

public interface AuthService {
    HttpStatus registration(RegistrationDTO registrationDTO, String url) throws MessagingException, IOException;

    TokenDTO login(LoginDTO loginDTO) throws JsonProcessingException;
}
