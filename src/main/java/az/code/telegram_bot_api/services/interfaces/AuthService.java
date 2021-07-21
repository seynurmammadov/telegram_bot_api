package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.LoginDTO;
import az.code.telegram_bot_api.models.RegistrationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;

public interface AuthService {
    RegistrationDTO registration(RegistrationDTO registrationDTO);
    HttpStatus verify(String confirmationToken);
    AccessTokenResponse login( LoginDTO loginDTO) throws JsonProcessingException;
}
