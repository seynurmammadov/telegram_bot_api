package az.code.telegram_bot_api.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidUserDataException extends CustomException {
    public InvalidUserDataException() {
        super("Invalid email or password!");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}