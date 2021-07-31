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
@ToString
public class Language implements Serializable,Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String langName;
    String keyword;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "language")
    List<Request> requests;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
