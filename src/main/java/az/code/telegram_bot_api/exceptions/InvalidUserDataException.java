package az.code.telegram_bot_api.exceptions;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException() {
        super("Invalid email or password!");
    }
}