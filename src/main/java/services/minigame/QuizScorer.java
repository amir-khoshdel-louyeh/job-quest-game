package services.minigame;

import java.util.ArrayList;
import java.util.List;

import model.minigame.MiniGameResult;
import model.minigame.QuizQuestion;

// Scores quiz rounds from correct answers and speed.
public class QuizScorer {

    // ----- Constants -----
    // Par time for a round.
    public static final int ROUND_PAR_SECONDS = 90;

    private static final double ACCURACY_WEIGHT = 0.8;
    private static final double SPEED_WEIGHT = 0.2;
    private static final double MAX_SPEED_RATIO = 1.0;

    // ----- Queries -----
    // Grades one quiz round.
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
        // Blend lives in ScoreMixer.
        double speedRatio = ScoreMixer.speedRatio(ROUND_PAR_SECONDS, elapsedSeconds, MAX_SPEED_RATIO);
        int score = 0;
        if (n > 0) {
            score = ScoreMixer.blend(accuracy, speedRatio * 100, ACCURACY_WEIGHT, SPEED_WEIGHT);
        }

        List<String> highlights = new ArrayList<>();
        highlights.add(correct + "/" + n + " correct");
        if (correct == n) highlights.add("perfect round 🌟");
        if (elapsedSeconds <= ROUND_PAR_SECONDS && n > 0) highlights.add("quick decisions ⚡");

        return MiniGameResult.quiz(gameName, score, accuracy, elapsedSeconds, highlights);
    }
}
