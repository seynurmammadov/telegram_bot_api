package az.code.telegram_bot_api.exceptions;

public class UserRequestNotFound extends RuntimeException {
    public UserRequestNotFound() {
        super("User request not found!");
    }
}