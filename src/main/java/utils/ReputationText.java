package utils;

// Reputation titles.
public final class ReputationText {
    private ReputationText() {
    }

    private static final int LEGENDARY_AT = 90;
    private static final int EXPERT_AT = 75;
    private static final int PROFESSIONAL_AT = 60;
    private static final int SKILLED_AT = 40;
    private static final int BEGINNER_AT = 20;

    public static String titleFor(int reputation) {
        if (reputation >= LEGENDARY_AT) {
            return "Legendary";
        }
        if (reputation >= EXPERT_AT) {
            return "Expert";
        }
        if (reputation >= PROFESSIONAL_AT) {
            return "Professional";
        }
        if (reputation >= SKILLED_AT) {
            return "Skilled";
        }
        if (reputation >= BEGINNER_AT) {
            return "Beginner";
        }
        return "Newbie";
    }
}
