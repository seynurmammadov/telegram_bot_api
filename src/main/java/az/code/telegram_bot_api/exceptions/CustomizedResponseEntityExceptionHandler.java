package az.code.telegram_bot_api.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Objects;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<Object> handleAllExceptions(CustomException ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .timestamp(new Date())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build(),
                ex.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(e -> sb.append(e.getDefaultMessage())
                        .append(System.getProperty("line.separator")));
        return new ResponseEntity<>(ExceptionResponse
                .builder()
                .timestamp(new Date())
                .message("Validation Failed")
                .details(sb.toString())
                .build(),
                HttpStatus.BAD_REQUEST);
    }
}
