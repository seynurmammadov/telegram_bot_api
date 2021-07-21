package az.code.telegram_bot_api.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
public class UserTokenDTO {
    String agent_name;
    String agent_surname;
    String email;
    String username;
    boolean verified;
}
