package az.code.telegram_bot_api.models.enums;

import lombok.Getter;

public enum AddressType {
    YOURSELF("YOURSELF"),
    AGENCY_PROPOSALS("AGENCY_PROPOSALS");
    @Getter
    private final String val;

    AddressType(String val) {
        this.val = val;
    }

    public static AddressType valueOfAddress(String val) {
        for (AddressType e : values()) {
            if (e.val.equals(val)) {
                return e;
            }
        }
        return null;
    }
}
