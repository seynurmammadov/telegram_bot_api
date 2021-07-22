package az.code.telegram_bot_api.models.DTOs;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserData implements Serializable {
    Long langId;
    String UUID;
    Map<String, String> answers = new HashMap<>();
}
