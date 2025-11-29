package utils;

import java.util.List;

// Mini-game one-line summaries.
public final class GameSummaryText {
    private GameSummaryText() {
    }

    public static String summaryLine(String gameName, int score, int accuracy,
                                     int elapsedSeconds, List<String> highlights) {
        String base = gameName + ": score " + score + "/100, accuracy " + accuracy
                + "% in " + elapsedSeconds + "s";
        if (highlights != null && !highlights.isEmpty()) {
            base = base + " — " + String.join("; ", highlights);
        }
        return base;
    }
}
