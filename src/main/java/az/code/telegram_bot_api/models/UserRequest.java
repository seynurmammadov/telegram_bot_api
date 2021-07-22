package az.code.telegram_bot_api.models;

import az.code.telegram_bot_api.models.enums.RequestStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_requests")
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, name = "user_id")
    User user;
    @ManyToOne(targetEntity = Request.class)
    @JoinColumn(nullable = false, name = "request_id")
    Request request;
    RequestStatus requestStatus;
}
