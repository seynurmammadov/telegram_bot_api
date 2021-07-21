package az.code.telegram_bot_api.exceptions;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException() {
        super("Token invalid!");
    }
}