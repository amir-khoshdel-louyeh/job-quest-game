package services.minigame;

import java.util.ArrayList;
import java.util.List;

import model.minigame.MiniGameResult;
import model.minigame.QuizQuestion;

/**
 * Pure scoring logic for quiz games (Doctor diagnosis, Designer briefs).
 *
 * <p>Stateless injectable service (SRP, DIP). 3 questions per round:
 * 80 points for correctness share + up to 20 speed bonus against par.
 * Wrong answers still pay a small consolation via the 0.5x floor in
 * {@link MiniGameResult#rewardMultiplier()}.</p>
 */
public class QuizScorer {

    /** Par seconds for the whole 3-question round. */
    public static final int ROUND_PAR_SECONDS = 90;

    /**
     * Grade one round.
     *
     * @param gameName shown in the result (e.g. "Diagnosis Round")
     * @param questions the asked questions (in order)
     * @param picked answers by index, -1 for timeout/skip
     * @param elapsedSeconds measured by the dialog
     */
    public MiniGameResult score(String gameName, List<QuizQuestion> questions,
                                List<Integer> picked, int elapsedSeconds) {
        int n = questions.size();
        int correct = 0;
        for (int i = 0; i < n; i++) {
            int p = (picked != null && i < picked.size()) ? picked.get(i) : -1;
            if (p >= 0 && questions.get(i).isCorrect(p)) correct++;
        }
        int accuracy = n == 0 ? 0 : (int) Math.round(correct * 100.0 / n);
        double speedRatio = Math.min(1.0, ROUND_PAR_SECONDS / (double) Math.max(1, elapsedSeconds));
        int score = n == 0 ? 0
                : (int) Math.round(accuracy * 0.8 + speedRatio * 100 * 0.2);

        List<String> highlights = new ArrayList<>();
        highlights.add(correct + "/" + n + " correct");
        if (correct == n) highlights.add("perfect round 🌟");
        if (elapsedSeconds <= ROUND_PAR_SECONDS && n > 0) highlights.add("quick decisions ⚡");

        return MiniGameResult.quiz(gameName, score, accuracy, elapsedSeconds, highlights);
    }
}
