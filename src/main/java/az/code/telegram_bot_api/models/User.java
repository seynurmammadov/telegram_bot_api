package az.code.telegram_bot_api.models;

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
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @Column(nullable = false, unique = true)
    String username;
    @Column(nullable = false, unique = true)
    String email;
    String company_name;
    Long tin;
    String agent_name;
    String agent_surname;
    LocalDateTime created_at;
    @OneToOne(mappedBy = "user")
    private VerificationToken verificationToken;

}
