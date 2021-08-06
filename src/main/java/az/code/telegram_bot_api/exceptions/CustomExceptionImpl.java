package az.code.telegram_bot_api.exceptions;

import org.springframework.http.HttpStatus;

public class CustomExceptionImpl extends CustomException {
    HttpStatus status;
    public CustomExceptionImpl(String message,int status) {
        super(message);
        this.status = HttpStatus.valueOf(status);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
