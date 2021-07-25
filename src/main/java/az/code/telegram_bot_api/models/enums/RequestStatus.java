package az.code.telegram_bot_api.models.enums;

public enum RequestStatus {
    NEW_REQUEST("newRequest"),
    OFFER_MADE("offerMade"),
    ACCEPTED("accepted"),
    EXPIRED("expired"),
    USER_CANCEL("userCancel");

    private final String val;

    RequestStatus(String val) {
        this.val = val;
    }

    public static RequestStatus valueOfStatus(String val) {
        for (RequestStatus e : values()) {
            if (e.val.equals(val)) {
                return e;
            }
        }
        return null;
    }
}
