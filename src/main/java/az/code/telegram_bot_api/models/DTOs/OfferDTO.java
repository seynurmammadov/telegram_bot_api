package az.code.telegram_bot_api.models.DTOs;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfferDTO {
    @Max(9999999999L)
    Long price;
    @Size(max = 1000, message = "Agent name should be less than 1000 characters")
    @NotNull
    String description;
    @Size(max = 200, message = "Agent name should be less than 200 characters")
    @NotNull
    String notes;
    @NotNull
    @Pattern(regexp = "^((0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).(19|20)\\d\\d-(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).(19|20)\\d\\d)$",
            message = "Invalid date interim!(Date should be like: 'xx.xx.xxxx-xx.xx.xxxx'  )")
    String dateInterim;
}
