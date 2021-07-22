package az.code.telegram_bot_api.models.enums;

public enum AddressType {
    YOURSELF("YOURSELF"),
    AGENCY_PROPOSALS("AGENCY_PROPOSALS");

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
