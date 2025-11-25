package model.minigame;

import java.util.ArrayList;
import java.util.List;

// Holds the score and summary of a mini-game.
public final class MiniGameResult {
    // Pass threshold shared by the quiz scorer and result texts (was a
    // magic 50 in three places).
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

    // A score of PASS_SCORE or more counts as a pass.
    public static MiniGameResult quiz(String gameName, int score, int accuracy,
                                      int elapsedSeconds, List<String> highlights) {
        boolean pass = false;
        if (score >= PASS_SCORE) {
            pass = true;
        }
        return new MiniGameResult(gameName, score, accuracy, elapsedSeconds, pass, highlights);
    }

    // Keeps the value between 0 and MAX_SCORE.
    private static int makeSafe(int v) {
        if (v < 0) {
            return 0;
        }
        if (v > MAX_SCORE) {
            return MAX_SCORE;
        }
        return v;
    }

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

    // Higher scores give a bigger reward, from REWARD_BASE to REWARD_BASE + REWARD_RANGE.
    public double rewardMultiplier() {
        return REWARD_BASE + score * REWARD_RANGE / MAX_SCORE;
    }
}
