package services.minigame;

import java.util.ArrayList;
import java.util.List;

import model.minigame.MiniGameResult;
import model.minigame.QuizQuestion;
import model.minigame.ScoreStrategy;

// Scores quiz rounds by blending accuracy (80%) with speed (20%).
public final class QuizScorer implements ScoreStrategy {

    // ----- Constants -----
    // Par time for a round.
    public static final int ROUND_PAR_SECONDS = 90;

    private static final double ACCURACY_WEIGHT = 0.8;
    private static final double SPEED_WEIGHT = 0.2;
    private static final double MAX_SPEED_RATIO = 1.0;

    // ----- Queries -----
    // Grades one quiz round.
    @Override
    public MiniGameResult score(String gameName, List<QuizQuestion> questions,
                                List<Integer> picked, int elapsedSeconds) {
        int n = questions.size();
        int correct = 0;
        for (int i = 0; i < n; i++) {
            int p = -1;
            if (picked != null && i < picked.size()) {
                p = picked.get(i);
            }
            if (p >= 0 && questions.get(i).isCorrect(p)) {
                correct = correct + 1;
            }
        }
        int accuracy = 0;
        if (n > 0) {
            accuracy = (int) Math.round(correct * 100.0 / n);
        }
        // Speed contributes only up to par time; faster does not score higher.
        double speedRatio = speedRatio(ROUND_PAR_SECONDS, elapsedSeconds, MAX_SPEED_RATIO);
        int score = 0;
        if (n > 0) {
            score = blend(accuracy, speedRatio * 100, ACCURACY_WEIGHT, SPEED_WEIGHT);
        }

        List<String> highlights = new ArrayList<>();
        highlights.add(correct + "/" + n + " correct");
        if (correct == n) highlights.add("perfect round 🌟");
        if (elapsedSeconds <= ROUND_PAR_SECONDS && n > 0) highlights.add("quick decisions ⚡");

        return MiniGameResult.quiz(gameName, score, accuracy, elapsedSeconds, highlights);
    }

    // ----- Private helpers -----
    // Faster than par, capped at maxRatio. Floors sub-second times so the
    // division below can never use zero.
    private static double speedRatio(int parSeconds, int elapsedSeconds, double maxRatio) {
        int safeTime = elapsedSeconds < 1 ? 1 : elapsedSeconds;
        double ratio = parSeconds / (double) safeTime;
        return ratio > maxRatio ? maxRatio : ratio;
    }

    // Weighted sum on the 0..100 scale, rounded once.
    private static int blend(int accuracy, double speedHundredths,
                             double accuracyWeight, double speedWeight) {
        return (int) Math.round(accuracy * accuracyWeight + speedHundredths * speedWeight);
    }
}
