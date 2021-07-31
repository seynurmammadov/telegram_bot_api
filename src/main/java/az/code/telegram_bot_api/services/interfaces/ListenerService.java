package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.AcceptedOffer;
import az.code.telegram_bot_api.models.DTOs.UserData;

public interface ListenerService {
    void saveRequest(UserData userData);

    void cancelRequest(String UUID);

    void acceptedOffer(AcceptedOffer offer);
}
