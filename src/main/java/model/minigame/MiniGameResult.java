package model.minigame;

import java.util.ArrayList;
import java.util.List;

// Holds the score and summary of a mini-game.
// Built via quiz(); read by JobController.
public final class MiniGameResult {
    public static final int PASS_SCORE = 50;
    private static final int MAX_SCORE = 100;
    private static final double REWARD_BASE = 0.5;
    private static final double REWARD_RANGE = 1.0;
    private String gameName;
    private int score;
    private int accuracy;
    private int elapsedSeconds;
    private boolean passed;
    private ArrayList<String> highlights;

    // ----- Construction -----
    private MiniGameResult(String gameName, int score, int accuracy,
                           int elapsedSeconds, boolean passed, List<String> highlights) {
        this.gameName = gameName;
        this.score = makeSafe(score);
        this.accuracy = makeSafe(accuracy);
        this.elapsedSeconds = elapsedSeconds;
        if (this.elapsedSeconds < 0) {
            this.elapsedSeconds = 0;
        }
        this.passed = passed;
        this.highlights = new ArrayList<String>();
        if (highlights != null) {
            for (int i = 0; i < highlights.size(); i++) {
                this.highlights.add(highlights.get(i));
            }
        }
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
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < highlights.size(); i++) {
            copy.add(highlights.get(i));
        }
        return copy;
    }

    // ----- Queries -----
    // Bigger score gives a bigger reward.
    public double rewardMultiplier() {
        return REWARD_BASE + score * REWARD_RANGE / MAX_SCORE;
    }
}
