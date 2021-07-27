package az.code.telegram_bot_api.exceptions;

import org.springframework.http.HttpStatus;

public class UserRequestNotFound extends CustomException {
    public UserRequestNotFound() {
        super("User request not found!");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}