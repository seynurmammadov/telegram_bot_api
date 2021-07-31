package az.code.telegram_bot_api.exceptions;

import org.springframework.http.HttpStatus;

public class TokenInvalidException extends CustomException {
    public TokenInvalidException() {
        super("Token invalid!");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}