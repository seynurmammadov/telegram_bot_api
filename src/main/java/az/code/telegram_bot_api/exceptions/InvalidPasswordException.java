package az.code.telegram_bot_api.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Invalid password!");
    }
}
