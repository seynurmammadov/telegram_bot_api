package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.DTOs.UserData;

public interface ListenerService {
    void saveRequest(UserData userData);
}
