package az.code.telegram_bot_api.services.interfaces;

import az.code.telegram_bot_api.models.User;



public interface UserService {
    User findUserByEmail(String email);
    void save(User user);
    void activeAndSave(User user);
}
