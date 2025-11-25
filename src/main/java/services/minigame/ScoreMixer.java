package services.minigame;

// Shared accuracy+speed blending for the quiz scorer.
// Clamp + ratio cap + weighted sum live here (DRY); the scorer keeps
// only its own weights, par and highlights.
public final class ScoreMixer {
    private ScoreMixer() {
    }

    // Elapsed time below one second would divide by zero — floor it.
    public static int safeTime(int elapsedSeconds) {
        if (elapsedSeconds < 1) {
            return 1;
        }
        return elapsedSeconds;
    }

    // How much faster than par (1.0 = exactly par), capped at maxRatio
    // so superhuman speed cannot inflate the score without bound.
    public static double speedRatio(int parSeconds, int elapsedSeconds, double maxRatio) {
        double ratio = parSeconds / (double) safeTime(elapsedSeconds);
        if (ratio > maxRatio) {
            return maxRatio;
        }
        return ratio;
    }

    // Weighted sum on the 0..100 scale, rounded once. Weights travel
    // together (no 1 - w assumption) so callers read exactly.
    public static int blend(int accuracy, double speedHundredths,
                            double accuracyWeight, double speedWeight) {
        return (int) Math.round(accuracy * accuracyWeight + speedHundredths * speedWeight);
    }
}
