package az.code.telegram_bot_api.exceptions;

import org.springframework.http.HttpStatus;

public class NotWorkTimeException extends CustomException {
    public NotWorkTimeException() {
        super("The request hes been sent out of work time!");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}