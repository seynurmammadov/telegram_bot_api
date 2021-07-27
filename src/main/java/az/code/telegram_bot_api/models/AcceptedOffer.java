package az.code.telegram_bot_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Table(name = "accepted_offers")
public class AcceptedOffer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String UUID;
    String username;
    String phoneNumber;
    @Column(name = "user_name")
    String userName;
    String firstName;
    String lastName;
    Long userId;
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(targetEntity = UserRequest.class)
    @JoinColumn(nullable = false, name = "user_request_id")
    UserRequest userRequest;
}
