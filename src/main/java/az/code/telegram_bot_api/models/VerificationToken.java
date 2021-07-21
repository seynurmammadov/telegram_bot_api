package az.code.telegram_bot_api.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verification_tokens")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime createdDate = LocalDateTime.now() ;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
}
