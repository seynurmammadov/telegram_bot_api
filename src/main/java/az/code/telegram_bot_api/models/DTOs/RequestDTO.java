package az.code.telegram_bot_api.models.DTOs;

import az.code.telegram_bot_api.models.enums.AddressType;
import az.code.telegram_bot_api.models.enums.TourType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDTO {
    String language;
    String UUID;
    String travelStartDate;
    String travelEndDate;
    String tourType;
    String addressToUser;
    String addressFrom;
    String addressToType;
    String travellerCount;
    int budget;
}
