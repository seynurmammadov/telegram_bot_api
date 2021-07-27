package az.code.telegram_bot_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long price;
    String description;
    String notes;
    String dateInterim;
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(targetEntity = UserRequest.class)
    @JoinColumn(nullable = false, name = "user_request_id")
    UserRequest userRequest;
}
