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
public class AgencyOffer {
    Long id;
    String username;
    String UUID;
    Integer messageId;
    @Transient
    byte[] file;
    String filePath;
}
