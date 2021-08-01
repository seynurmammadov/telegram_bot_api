package az.code.telegram_bot_api.models.DTOs;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@FieldDefaults(level=AccessLevel.PRIVATE)
public class UserTokenDTO {
    String agent_name;
    String agent_surname;
    String email;
    String username;
    boolean verified;
}
