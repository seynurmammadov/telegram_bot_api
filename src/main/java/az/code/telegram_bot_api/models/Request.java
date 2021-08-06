package az.code.telegram_bot_api.models;

import az.code.telegram_bot_api.models.enums.AddressType;
import az.code.telegram_bot_api.models.enums.TourType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "requests")
@SequenceGenerator(name = "order_seq", initialValue = 1000000, allocationSize = 1)
public class Request implements Cloneable {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    Long orderId;
    @Column(columnDefinition = "TEXT")
    private String answers;

    String UUID;

    @EqualsAndHashCode.Exclude
    LocalDateTime createdAt;
    @EqualsAndHashCode.Exclude
    LocalDateTime experationDate;
    boolean isActive;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "request", fetch = FetchType.EAGER)
    List<UserRequest> userRequests = new ArrayList<>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
