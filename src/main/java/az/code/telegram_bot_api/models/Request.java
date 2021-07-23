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
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "requests")
@SequenceGenerator(name = "order_seq", initialValue = 1000000, allocationSize = 1)
public class Request {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    Long orderId;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "lang_id")
    Language language;
    String UUID;
    LocalDate travelStartDate;
    LocalDate travelEndDate;
    TourType tourType;
    String addressToUser;
    String addressFrom;
    AddressType addressToType;
    String travellerCount;
    int budget;
    LocalDateTime createdAt;
    LocalDateTime experationDate;
    boolean isExpired;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "request", fetch = FetchType.EAGER)
    List<UserRequest> userRequests = new ArrayList<>();
}
