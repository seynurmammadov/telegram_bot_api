package az.code.telegram_bot_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Table(name = "accepted_offers")
public class AcceptedOffer implements Serializable,Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String UUID;
    String agentUsername;
    String phoneNumber;
    String username;
    String firstName;
    String lastName;
    Long userId;
    @EqualsAndHashCode.Exclude
    LocalDateTime createdAt;
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(targetEntity = UserRequest.class)
    @JoinColumn(nullable = false, name = "user_request_id")
    UserRequest userRequest;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
