package services.minigame;

// Shared accuracy+speed blending for mini-game scorers (Strategy core).
// QuizScorer and TypingScorer had the same time-clamp, speed-ratio cap
// and weighted-sum lines copied — now both delegate here (DRY), each
// keeping only its own weights, par source and highlights (OCP: new game
// = new weights + new highlights, this class never changes).
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

    // Speed term on the 0..100 scale, clamped so it never exceeds a
    // perfect accuracy term.
    public static int cappedSpeedPoints(double speedRatio) {
        int points = (int) Math.round(speedRatio * 100);
        if (points < 0) {
            return 0;
        }
        if (points > 100) {
            return 100;
        }
        return points;
    }

    // Weighted sum on the 0..100 scale, rounded once. Weights travel
    // together (no 1 - w assumption) so callers read exactly.
    public static int blend(int accuracy, double speedHundredths,
                            double accuracyWeight, double speedWeight) {
        return (int) Math.round(accuracy * accuracyWeight + speedHundredths * speedWeight);
    }
}
