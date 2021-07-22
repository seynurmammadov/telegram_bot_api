package az.code.telegram_bot_api.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    boolean isActive;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",fetch = FetchType.EAGER)
    List<VerificationToken> verificationToken = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",fetch = FetchType.EAGER)
    Set<UserRequest> userRequests = new HashSet<>();
    public void addRequest(UserRequest request){
        userRequests.add(request);
    }
}
