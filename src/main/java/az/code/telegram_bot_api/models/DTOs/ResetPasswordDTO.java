package az.code.telegram_bot_api.models.DTOs;

import az.code.telegram_bot_api.annotations.FieldMatch;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldMatch(first = "password", second = "password_repeat", message = "The password fields must match")
@EqualsAndHashCode
@ToString
public class ResetPasswordDTO {
    String oldPassword;
    @Size(max = 15, min = 6, message = "Password should be less than 15 characters and bigger than 6 charters")
    @NotBlank(message = "Password must not be null or empty")
    String password;
    @Size(max = 15, min = 6, message = "Password repeat should be less than 15 characters and bigger than 6 charters")
    @NotBlank(message = "Password repeat must not be null or empty")
    String password_repeat;
}
