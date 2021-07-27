package az.code.telegram_bot_api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Setter
@Builder
public class ExceptionResponse {
    Date timestamp;
    String message;
    String details;
}