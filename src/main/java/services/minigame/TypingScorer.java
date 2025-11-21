package services.minigame;

import java.util.ArrayList;
import java.util.List;

import model.minigame.MiniGameResult;
import model.minigame.TextTask;

// Scores typing games from accuracy and speed.
public class TypingScorer {

    private static final double ACCURACY_WEIGHT = 0.65;
    private static final double SPEED_WEIGHT = 0.35;
    private static final double MAX_SPEED_RATIO = 1.5;
    private static final int CLEAN_COPY_AT = 85;
    private static final int TYPO_MESS_BELOW = 50;

    // Grades one typed attempt.
    public MiniGameResult score(String gameName, TextTask task, String typed, int elapsedSeconds) {
        String expected = normalize(task.getContent());
        String actual = normalize(typed);
        int accuracy = accuracy(expected, actual);
        int wpm = wpm(actual, elapsedSeconds);

        // Speed bonus: typing faster than the par time scores more.
        // Shared mix (DRY): clamp + ratio cap + weighted sum live in ScoreMixer.
        double speedRatio = ScoreMixer.speedRatio(task.getParSeconds(), elapsedSeconds, MAX_SPEED_RATIO);
        int speedPoints = ScoreMixer.cappedSpeedPoints(speedRatio);
        int score = ScoreMixer.blend(accuracy, speedPoints, ACCURACY_WEIGHT, SPEED_WEIGHT);

        List<String> highlights = new ArrayList<>();
        if (accuracy == 100) highlights.add("flawless ⌨️");
        else if (accuracy >= CLEAN_COPY_AT) highlights.add("clean copy");
        else if (accuracy < TYPO_MESS_BELOW) highlights.add("lots of typos");
        if (elapsedSeconds <= task.getParSeconds()) highlights.add("faster than par ⚡");

        return MiniGameResult.typing(gameName, score, accuracy, wpm, elapsedSeconds, highlights);
    }

    // Returns 0-100 text similarity.
    public int accuracy(String expected, String actual) {
        if (expected == null) {
            expected = "";
        }
        if (actual == null) {
            actual = "";
        }
        if (expected.equals("") && actual.equals("")) {
            return 100;
        }
        if (expected.equals("") || actual.equals("")) {
            return 0;
        }
        int dist = levenshtein(expected, actual);
        int max = expected.length();
        if (actual.length() > max) {
            max = actual.length();
        }
        return (int) Math.round((1.0 - dist / (double) max) * 100);
    }

    /** Public for tests: standard 5-chars = 1 word. */
    public int wpm(String normalizedText, int elapsedSeconds) {
        if (elapsedSeconds <= 0) return 0;
        double words = normalizedText.length() / 5.0;
        return (int) Math.round(words / (elapsedSeconds / 60.0));
    }

    // Trims extra spaces and blank lines.
    static String normalize(String s) {
        if (s == null) {
            return "";
        }
        // replace tab with spaces
        s = s.replace("\t", "    ");
        String[] lines = s.split("\n");
        String result = "";
        for (int i = 0; i < lines.length; i++) {
            // trim end spaces manually with trim
            String line = lines[i].trim();
            result = result + line;
            if (i < lines.length - 1) {
                result = result + "\n";
            }
        }
        return result.trim();
    }

    // Counts the edits needed to turn one text into the other.
    static int levenshtein(String a, String b) {
        int[] prev = new int[b.length() + 1];
        int[] cur = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) prev[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            cur[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int cost = 0;
                if (a.charAt(i - 1) != b.charAt(j - 1)) {
                    cost = 1;
                }
                int delete = prev[j] + 1;
                int insert = cur[j - 1] + 1;
                int replace = prev[j - 1] + cost;
                int best = delete;
                if (insert < best) {
                    best = insert;
                }
                if (replace < best) {
                    best = replace;
                }
                cur[j] = best;
            }
            int[] tmp = prev; prev = cur; cur = tmp;
        }
        return prev[b.length()];
    }
}
