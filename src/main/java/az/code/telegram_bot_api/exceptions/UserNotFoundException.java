package az.code.telegram_bot_api.exceptions;

public class UserNotFoundException  extends RuntimeException {
    public UserNotFoundException() {
        super("User not found!");
    }
}