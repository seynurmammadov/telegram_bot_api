package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.User;


public interface UserService {
    User findUserByEmail(String email);

    User save(User user);

    User activeAndSave(User user);

    void addRequestToUsers(Request request);
}
