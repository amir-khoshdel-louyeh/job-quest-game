package services.minigame;

import java.util.ArrayList;
import java.util.List;

import model.minigame.MiniGameResult;
import model.minigame.TextTask;

/**
 * Pure scoring logic for retype games (Typist prose, Programmer code).
 *
 * <p>Stateless injectable service (SRP, DIP): no UI, no timers — the dialog
 * measures time, this class only grades. Accuracy uses Levenshtein distance
 * over normalized text; speed compares elapsed time against the task par.</p>
 */
public class TypingScorer {

    /**
     * Grade one attempt.
     *
     * @param gameName shown in the result (e.g. "Typing Sprint")
     * @param task the picked task
     * @param typed what the player wrote
     * @param elapsedSeconds measured by the dialog
     */
    public MiniGameResult score(String gameName, TextTask task, String typed, int elapsedSeconds) {
        String expected = normalize(task.getContent());
        String actual = normalize(typed == null ? "" : typed);
        int accuracy = accuracy(expected, actual);
        int wpm = wpm(actual, elapsedSeconds);

        double speedRatio = Math.min(1.5, task.getParSeconds() / (double) Math.max(1, elapsedSeconds));
        int speedPoints = (int) Math.round(Math.min(100, speedRatio * 100));
        int score = (int) Math.round(accuracy * 0.65 + speedPoints * 0.35);

        List<String> highlights = new ArrayList<>();
        if (accuracy == 100) highlights.add("flawless ⌨️");
        else if (accuracy >= 85) highlights.add("clean copy");
        else if (accuracy < 50) highlights.add("lots of typos");
        if (elapsedSeconds <= task.getParSeconds()) highlights.add("faster than par ⚡");

        return MiniGameResult.typing(gameName, score, accuracy, wpm, elapsedSeconds, highlights);
    }

    /** Public for tests: 0–100 similarity of two normalized strings. */
    public int accuracy(String expected, String actual) {
        if (expected.isEmpty() && actual.isEmpty()) return 100;
        if (expected.isEmpty() || actual.isEmpty()) return 0;
        int dist = levenshtein(expected, actual);
        int max = Math.max(expected.length(), actual.length());
        return (int) Math.round((1.0 - dist / (double) max) * 100);
    }

    /** Public for tests: standard 5-chars = 1 word. */
    public int wpm(String normalizedText, int elapsedSeconds) {
        if (elapsedSeconds <= 0) return 0;
        double words = normalizedText.length() / 5.0;
        return (int) Math.round(words / (elapsedSeconds / 60.0));
    }

    /** Trim trailing spaces per line; keep everything else (code matters). */
    static String normalize(String s) {
        String[] lines = s.replace("\t", "    ").split("\n");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            sb.append(lines[i].stripTrailing());
            if (i < lines.length - 1) sb.append('\n');
        }
        return sb.toString().strip();
    }

    /** Classic DP Levenshtein over chars. */
    static int levenshtein(String a, String b) {
        int[] prev = new int[b.length() + 1];
        int[] cur = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) prev[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            cur[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                cur[j] = Math.min(Math.min(cur[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev; prev = cur; cur = tmp;
        }
        return prev[b.length()];
    }
}
