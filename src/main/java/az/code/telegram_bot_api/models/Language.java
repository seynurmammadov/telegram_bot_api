package az.code.telegram_bot_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@EqualsAndHashCode
@Table(name = "languages")
public class Language implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String langName;
    String keyword;
    @JsonIgnore
    @OneToMany(mappedBy = "language")
    List<Request> requests;
}
