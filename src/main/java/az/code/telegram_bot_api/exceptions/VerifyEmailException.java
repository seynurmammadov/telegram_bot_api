package az.code.telegram_bot_api.exceptions;

import org.springframework.http.HttpStatus;

public class VerifyEmailException extends CustomException {
    public VerifyEmailException() {
        super("Verify email before login!");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
