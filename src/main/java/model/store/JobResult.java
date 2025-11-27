package model.store;

import java.util.List;

import model.career.Job;
import model.goals.Achievement;
import model.goals.Challenge;

// Holds the outcome of doing a job: success flag + display message.
// Built via success()/failure(); read by JobController and views.
public final class JobResult {
    private final boolean success;
    private final String message;

    // ----- Construction -----
    private JobResult(boolean success, String message) {
        this.success = success;
        if (message == null) {
            this.message = "";
        } else {
            this.message = message;
        }
    }

    // ----- Factories -----
    // Builds a success; message wording lives in JobResultText.
    public static JobResult success(Job job, int basePayment, int bonusAmount, int finalPayment,
                                    int xpGained, int reputationGained,
                                    int levelBefore, int levelAfter,
                                    List<Challenge> completedChallenges,
                                    List<Achievement> newAchievements) {
        String msg = utils.JobResultText.successLine(job, basePayment, bonusAmount, finalPayment,
                xpGained, reputationGained, levelBefore, levelAfter, completedChallenges, newAchievements);
        return new JobResult(true, msg);
    }

    public static JobResult success(Job job, int basePayment, int bonusAmount, int finalPayment,
                                    int xpGained, int reputationGained,
                                    int levelBefore, int levelAfter,
                                    List<Challenge> completedChallenges,
                                    List<Achievement> newAchievements,
                                    model.minigame.MiniGameResult game) {
        JobResult base = success(job, basePayment, bonusAmount, finalPayment, xpGained,
                reputationGained, levelBefore, levelAfter, completedChallenges, newAchievements);
        if (game == null) {
            return base;
        }
        String withGame = utils.JobResultText.successWithGameLine(base.getMessage(), game);
        return new JobResult(true, withGame);
    }

    // Builds a failure; message wording lives in JobResultText.
    public static JobResult failure(Job job, String reason) {
        String msg = utils.JobResultText.failureLine(job, reason);
        return new JobResult(false, msg);
    }

    // Builds a failed attempt; message wording lives in JobResultText.
    public static JobResult failedAttempt(Job job, int score, int energyLost, int repLost,
                                          model.minigame.MiniGameResult game) {
        String msg = utils.JobResultText.failedAttemptLine(job, score, energyLost, repLost, game);
        return new JobResult(false, msg);
    }

    // ----- Getter -----
    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
}
