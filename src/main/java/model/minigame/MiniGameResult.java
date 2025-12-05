package model.minigame;

import java.util.ArrayList;
import java.util.List;

import utils.Lists;
import utils.ProgressMath;

// Holds the score and summary of a mini-game.
// Built via quiz(); read by JobController.
public final class MiniGameResult {
    public static final int PASS_SCORE = 50;
    private static final int MAX_SCORE = 100;
    private static final double REWARD_BASE = 0.5;
    private static final double REWARD_RANGE = 1.0;
    private final String gameName;
    private final int score;
    private final int accuracy;
    private final int elapsedSeconds;
    private final boolean passed;
    private final ArrayList<String> highlights;

    // ----- Construction -----
    private MiniGameResult(String gameName, int score, int accuracy,
                           int elapsedSeconds, boolean passed, List<String> highlights) {
        this.gameName = gameName;
        this.score = makeSafe(score);
        this.accuracy = makeSafe(accuracy);
        this.elapsedSeconds = Math.max(elapsedSeconds, 0);
        this.passed = passed;
        this.highlights = new ArrayList<String>(Lists.copyOf(highlights));
    }

    // Builds a quiz result; score >= PASS_SCORE passes.
    public static MiniGameResult quiz(String gameName, int score, int accuracy,
                                      int elapsedSeconds, List<String> highlights) {
        boolean pass = false;
        if (score >= PASS_SCORE) {
            pass = true;
        }
        return new MiniGameResult(gameName, score, accuracy, elapsedSeconds, pass, highlights);
    }

    // ----- Private helpers -----
    // Clamps the value to 0..MAX_SCORE.
    private static int makeSafe(int v) {
        if (v < 0) {
            return 0;
        }
        if (v > MAX_SCORE) {
            return MAX_SCORE;
        }
        return v;
    }

    // ----- Getter -----
    public String getGameName() {
        return gameName;
    }
    public int getScore() {
        return score;
    }
    public int getAccuracy() {
        return accuracy;
    }
    public int getElapsedSeconds() {
        return elapsedSeconds;
    }
    public boolean isPassed() {
        return passed;
    }
    public List<String> getHighlights() {
        return Lists.copyOf(highlights);
    }

    // ----- Queries -----
    // Bigger score gives a bigger reward.
    public double rewardMultiplier() {
        double scorePercent = ProgressMath.percent(score, MAX_SCORE);
        return REWARD_BASE + scorePercent / 100.0 * REWARD_RANGE;
    }
}
