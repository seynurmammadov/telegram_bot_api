package az.code.telegram_bot_api.models.DTOs;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class TokenDTO {
    String access_token;
}
