package az.code.telegram_bot_api.models.enums;

public enum TourType {
    RELAX_AND_WALK("RELAX_AND_WALK"),
    EXCURSION("EXCURSION"),
    DOES_NOT_MATTER("DOES_NOT_MATTER"),
    EXTREME("EXTREME");
    private final String val;

    TourType(String val) {
        this.val = val;
    }

    public static TourType valueOfTour(String val) {
        for (TourType e : values()) {
            if (e.val.equals(val)) {
                return e;
            }
        }
        return null;
    }
}
