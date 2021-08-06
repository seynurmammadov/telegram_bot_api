package az.code.telegram_bot_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyOffer implements Cloneable {
    Long id;
    String username;
    String UUID;
    Integer messageId;
    @Transient
    byte[] file;
    String filePath;
    boolean isAccepted;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
