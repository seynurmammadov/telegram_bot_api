package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.DTOs.UserTokenDTO;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.UserRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface RequestService {
    List<UserRequest> getAll(UserTokenDTO userTokenDTO, Pageable pageable);
    HttpStatus archiveRequest(UserTokenDTO userTokenDTO, Long userRequestId);
}
