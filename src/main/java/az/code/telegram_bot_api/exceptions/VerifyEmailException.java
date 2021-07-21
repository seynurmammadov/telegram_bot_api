package az.code.telegram_bot_api.exceptions;

public class VerifyEmailException extends RuntimeException {
    public VerifyEmailException() {
        super("Verify email before login!");
    }
}
