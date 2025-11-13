package services.minigame;

import java.util.ArrayList;
import java.util.List;

import model.minigame.MiniGameResult;
import model.minigame.QuizQuestion;

// This class calculates score for quiz mini-games.
// Simple helper with no UI.
public class QuizScorer {

    // Par time for round
    public static final int ROUND_PAR_SECONDS = 90;

    // Grade one round, uses simple for loop and if.
    public MiniGameResult score(String gameName, List<QuizQuestion> questions,
                                List<Integer> picked, int elapsedSeconds) {
        int n = questions.size();
        int correct = 0;
        for (int i = 0; i < n; i++) {
            int p = -1;
            if (picked != null && i < picked.size()) {
                p = picked.get(i);
            }
            if (p >= 0) {
                if (questions.get(i).isCorrect(p)) {
                    correct = correct + 1;
                }
            }
        }
        int accuracy = 0;
        if (n > 0) {
            accuracy = (int) Math.round(correct * 100.0 / n);
        }
        int timeForMath = elapsedSeconds;
        if (timeForMath < 1) {
            timeForMath = 1;
        }
        double speedRatio = ROUND_PAR_SECONDS / (double) timeForMath;
        if (speedRatio > 1.0) {
            speedRatio = 1.0;
        }
        int score = 0;
        if (n > 0) {
            score = (int) Math.round(accuracy * 0.8 + speedRatio * 100 * 0.2);
        }

        List<String> highlights = new ArrayList<>();
        highlights.add(correct + "/" + n + " correct");
        if (correct == n) highlights.add("perfect round 🌟");
        if (elapsedSeconds <= ROUND_PAR_SECONDS && n > 0) highlights.add("quick decisions ⚡");

        return MiniGameResult.quiz(gameName, score, accuracy, elapsedSeconds, highlights);
    }
}
