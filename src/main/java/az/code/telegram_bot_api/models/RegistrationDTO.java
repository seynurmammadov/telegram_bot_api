package az.code.telegram_bot_api.models;

import az.code.telegram_bot_api.annotations.FieldMatch;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldMatch(first = "password", second = "password_repeat", message = "The password fields must match")
public class RegistrationDTO {
    String username;
    @Size(max = 30, message = "Company name should be less than 30 characters")
    @NotNull
    String company_name;
    @Max(9999999999L)
    Long tin;
    @Size(max = 30, message = "Agent name should be less than 30 characters")
    @NotNull
    String agent_name;
    @Size(max = 30, message = "Agent surname should be less than 30 characters")
    @NotNull
    String agent_surname;
    @Size(max = 30, message = "Email should be less than 30 characters")
    @Email(message = "Email is not valid")
    @NotNull
    String email;
    @Size(max = 15, min = 6, message = "Password  should be less than 15 characters")
    @NotNull
    String password;
    @Size(max = 15, min = 6, message = "Password repeat should be less than 15 characters")
    @NotNull
    String password_repeat;

    int statusCode;
    String status;
}

