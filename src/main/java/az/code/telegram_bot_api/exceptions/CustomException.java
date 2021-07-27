package az.code.telegram_bot_api.exceptions;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    public abstract HttpStatus getStatus();

    public CustomException(String message) {
        super(message);
    }
}
