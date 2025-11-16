package utils;

import java.util.List;

// Single home for mini-game one-line summaries.
// Keeps string building out of the MiniGameResult DTO.
public final class GameSummaryText {
    private GameSummaryText() {
    }

    public static String summaryLine(String gameName, int score, int accuracy,
                                     int wpm, int elapsedSeconds, List<String> highlights) {
        String base;
        if (wpm > 0) {
            base = gameName + ": score " + score + "/100, accuracy " + accuracy + "%, " + wpm + " WPM in " + elapsedSeconds + "s";
        } else {
            base = gameName + ": score " + score + "/100, accuracy " + accuracy + "% in " + elapsedSeconds + "s";
        }
        if (highlights != null && !highlights.isEmpty()) {
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
