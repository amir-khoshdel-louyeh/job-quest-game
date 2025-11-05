package model.minigame;

import java.util.Collections;
import java.util.List;

/**
 * Immutable result of a work-time mini-game.
 *
 * <p>OOP <b>Encapsulation</b>: all fields final, getters only, created via
 * static factories. The {@code score} (0–100) drives the job reward
 * multiplier in {@code JobController}: {@code 0.5 + score/100}
 * (score 0 → half pay, 100 → 1.5x pay).</p>
 */
public final class MiniGameResult {
    private final String gameName;
    private final int score;
    private final int accuracy;
    private final int wpm;
    private final int elapsedSeconds;
    private final boolean passed;
    private final List<String> highlights;

    private MiniGameResult(String gameName, int score, int accuracy, int wpm,
                           int elapsedSeconds, boolean passed, List<String> highlights) {
        this.gameName = gameName;
        this.score = clamp(score);
        this.accuracy = clamp(accuracy);
        this.wpm = Math.max(0, wpm);
        this.elapsedSeconds = Math.max(0, elapsedSeconds);
        this.passed = passed;
        this.highlights = highlights == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new java.util.ArrayList<>(highlights));
    }

    /** Typing-like result (has WPM). */
    public static MiniGameResult typing(String gameName, int score, int accuracy,
                                        int wpm, int elapsedSeconds, List<String> highlights) {
        return new MiniGameResult(gameName, score, accuracy, wpm,
                elapsedSeconds, score >= 50, highlights);
    }

    /** Quiz-like result (no WPM). */
    public static MiniGameResult quiz(String gameName, int score, int accuracy,
                                      int elapsedSeconds, List<String> highlights) {
        return new MiniGameResult(gameName, score, accuracy, 0,
                elapsedSeconds, score >= 50, highlights);
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }

    public String getGameName() { return gameName; }
    public int getScore() { return score; }
    public int getAccuracy() { return accuracy; }
    public int getWpm() { return wpm; }
    public int getElapsedSeconds() { return elapsedSeconds; }
    public boolean isPassed() { return passed; }
    public List<String> getHighlights() { return highlights; }

    /** Reward multiplier: 0.5x (score 0) … 1.5x (score 100). */
    public double rewardMultiplier() {
        return 0.5 + score / 100.0;
    }

    /** One-line summary appended to the job message. */
    public String summaryLine() {
        String base = wpm > 0
                ? String.format("🎮 %s: score %d/100, accuracy %d%%, %d WPM in %ds",
                        gameName, score, accuracy, wpm, elapsedSeconds)
                : String.format("🎮 %s: score %d/100, accuracy %d%% in %ds",
                        gameName, score, accuracy, elapsedSeconds);
        if (!highlights.isEmpty()) {
            base += " — " + String.join("; ", highlights);
        }
        return base;
    }
}
