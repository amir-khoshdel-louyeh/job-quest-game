package model.minigame;

import java.util.ArrayList;
import java.util.List;

// Result of mini-game: score, accuracy, etc.
// Simple data class with getters (Encapsulation).
public final class MiniGameResult {
    private String gameName;
    private int score;
    private int accuracy;
    private int wpm;
    private int elapsedSeconds;
    private boolean passed;
    private ArrayList<String> highlights;

    private MiniGameResult(String gameName, int score, int accuracy, int wpm,
                           int elapsedSeconds, boolean passed, List<String> highlights) {
        this.gameName = gameName;
        this.score = makeSafe(score);
        this.accuracy = makeSafe(accuracy);
        this.wpm = wpm;
        if (this.wpm < 0) {
            this.wpm = 0;
        }
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

    // Make typing result
    public static MiniGameResult typing(String gameName, int score, int accuracy,
                                        int wpm, int elapsedSeconds, List<String> highlights) {
        boolean pass = false;
        if (score >= 50) {
            pass = true;
        }
        return new MiniGameResult(gameName, score, accuracy, wpm, elapsedSeconds, pass, highlights);
    }

    // Make quiz result
    public static MiniGameResult quiz(String gameName, int score, int accuracy,
                                      int elapsedSeconds, List<String> highlights) {
        boolean pass = false;
        if (score >= 50) {
            pass = true;
        }
        return new MiniGameResult(gameName, score, accuracy, 0, elapsedSeconds, pass, highlights);
    }

    // Keep value 0-100 with simple if (no Math).
    private static int makeSafe(int v) {
        if (v < 0) {
            return 0;
        }
        if (v > 100) {
            return 100;
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
    public int getWpm() {
        return wpm;
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

    // Reward: 0.5x to 1.5x based on score
    public double rewardMultiplier() {
        return 0.5 + score / 100.0;
    }

    // One line summary, uses + (no String.format, no String.join).
    public String summaryLine() {
        String base = "";
        if (wpm > 0) {
            base = gameName + ": score " + score + "/100, accuracy " + accuracy + "%, " + wpm + " WPM in " + elapsedSeconds + "s";
        } else {
            base = gameName + ": score " + score + "/100, accuracy " + accuracy + "% in " + elapsedSeconds + "s";
        }
        if (!highlights.isEmpty()) {
            String h = "";
            for (int i = 0; i < highlights.size(); i++) {
                if (i > 0) {
                    h = h + "; ";
                }
                h = h + highlights.get(i);
            }
            base = base + " — " + h;
        }
        return base;
    }
}
