package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.DTOs.LoginDTO;
import az.code.telegram_bot_api.models.DTOs.RegistrationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.keycloak.representations.AccessTokenResponse;

public interface AuthService {
    RegistrationDTO registration(RegistrationDTO registrationDTO, String url);

    AccessTokenResponse login(LoginDTO loginDTO) throws JsonProcessingException;
}
