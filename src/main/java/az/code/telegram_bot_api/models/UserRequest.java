package az.code.telegram_bot_api.models;

import az.code.telegram_bot_api.models.enums.RequestStatus;
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
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_requests")
public class UserRequest implements Cloneable{
    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(nullable = false, name = "user_id")
    User user;
    @ManyToOne(targetEntity = Request.class)
    @EqualsAndHashCode.Exclude
    @JoinColumn(nullable = false, name = "request_id")
    Request request;
    RequestStatus requestStatus;
    boolean isArchived;
    boolean isDeleted;
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "userRequest", cascade = CascadeType.ALL)
    Offer offer;
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "userRequest", cascade = CascadeType.ALL)
    AcceptedOffer acceptedOffer;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
