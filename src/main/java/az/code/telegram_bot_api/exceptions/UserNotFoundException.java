package az.code.telegram_bot_api.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException  extends CustomException {
    public UserNotFoundException() {
        super("User not found!");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}