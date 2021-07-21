package az.code.telegram_bot_api.exceptions;

public class IncorrectVerifyTokenException extends RuntimeException {
    public IncorrectVerifyTokenException() {
        super("Verify token incorrect!");
    }
}