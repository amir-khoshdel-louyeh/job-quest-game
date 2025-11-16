package utils;

// Single home for reputation titles. Keeps view words out of CareerProgress.
public final class ReputationText {
    private ReputationText() {
    }

    public static String titleFor(int reputation) {
        if (reputation >= 90) {
            return "Legendary";
        }
        if (reputation >= 75) {
            return "Expert";
        }
        if (reputation >= 60) {
            return "Professional";
        }
        if (reputation >= 40) {
            return "Skilled";
        }
        if (reputation >= 20) {
            return "Beginner";
        }
        return "Newbie";
    }
}
