package az.code.telegram_bot_api.models;

import az.code.telegram_bot_api.annotations.FieldMatch;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@FieldMatch(first = "password", second = "password_repeat", message = "The password fields must match")
public class ResetPasswordDTO {
    @Size(max = 15, min = 6, message = "Password  should be less than 15 characters")
    @NotNull
    String password;
    @Size(max = 15, min = 6, message = "Password repeat should be less than 15 characters")
    @NotNull
    String password_repeat;
}
