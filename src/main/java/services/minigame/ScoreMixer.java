package services.minigame;

// Blends accuracy + speed for the quiz scorer.
public final class ScoreMixer {
    private ScoreMixer() {
    }

    // ----- Queries -----
    // Floors sub-second times to avoid divide-by-zero.
    public static int safeTime(int elapsedSeconds) {
        if (elapsedSeconds < 1) {
            return 1;
        }
        return elapsedSeconds;
    }

    // Faster than par, capped at maxRatio.
    public static double speedRatio(int parSeconds, int elapsedSeconds, double maxRatio) {
        double ratio = parSeconds / (double) safeTime(elapsedSeconds);
        if (ratio > maxRatio) {
            return maxRatio;
        }
        return ratio;
    }

    // Weighted sum on the 0..100 scale, rounded once.
    public static int blend(int accuracy, double speedHundredths,
                            double accuracyWeight, double speedWeight) {
        return (int) Math.round(accuracy * accuracyWeight + speedHundredths * speedWeight);
    }
}
