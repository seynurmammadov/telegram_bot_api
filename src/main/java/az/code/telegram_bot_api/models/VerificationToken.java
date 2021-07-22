package az.code.telegram_bot_api.models;

import az.code.telegram_bot_api.models.enums.TokenType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verification_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String token;
    TokenType tokenType;
    LocalDateTime createdDate = LocalDateTime.now();
    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, name = "user_id")
    User user;
}
