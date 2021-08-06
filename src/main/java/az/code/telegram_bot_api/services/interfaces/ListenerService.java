package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.AcceptedOffer;
import az.code.telegram_bot_api.models.DTOs.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ListenerService {
    void saveRequest(UserData userData) throws JsonProcessingException;

    void cancelRequest(String UUID);

    void acceptedOffer(AcceptedOffer offer);
}
