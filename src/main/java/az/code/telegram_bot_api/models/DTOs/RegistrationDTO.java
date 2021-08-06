package az.code.telegram_bot_api.models.DTOs;

import az.code.telegram_bot_api.annotations.FieldMatch;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldMatch(first = "password", second = "password_repeat", message = "The password fields must match!")
public class RegistrationDTO {
    String username;
    @Size(max = 30, message = "Company name should be less than 30 characters")
    @NotBlank(message = "Company name must not be null or empty")
    String company_name;
    @Max(value = 9999999999L,message = "TIN should be smaller than 9999999999")
    @Positive(message = "TIN should be positive")
    Long tin;
    @Size(max = 30, message = "Agent name should be less than 30 characters")
    @NotBlank(message = "Agent name must not be null or empty")
    String agent_name;
    @Size(max = 30, message = "Agent surname should be less than 30 characters")
    @NotBlank(message = "Agent surname must not be null or empty")
    String agent_surname;
    @Size(max = 30, message = "Email should be less than 30 characters")
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email must not be null or empty")
    String email;
    @Size(max = 15, min = 6, message = "Password should be less than 15 characters and bigger than 6 charters")
    @NotBlank(message = "Password must not be null or empty")
    String password;
    @Size(max = 15, min = 6, message = "Password repeat should be less than 15 characters and bigger than 6 charters")
    @NotBlank(message = "Password repeat must not be null or empty")
    String password_repeat;
}

