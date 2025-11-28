package utils;

import java.util.List;

import model.career.Job;
import model.goals.Achievement;
import model.goals.Challenge;
import model.minigame.MiniGameResult;

// Job-result message texts.
// Owns wording; JobResult holds data.
public final class JobResultText {
    private JobResultText() {
    }

    // ----- Queries -----
    // Builds the success message.
    public static String successLine(Job job, int basePayment, int bonusAmount, int finalPayment,
                                     int xpGained, int reputationGained,
                                     int levelBefore, int levelAfter,
                                     List<Challenge> completedChallenges,
                                     List<Achievement> newAchievements) {
        String msg = job.getName() + " completed! +$" + finalPayment;
        msg = msg + " (base $" + basePayment + " + bonus $" + bonusAmount + ")";
        msg = msg + " +" + xpGained + " XP +" + reputationGained + " rep";
        if (levelAfter > levelBefore) {
            msg = msg + " Level up! " + levelBefore + " -> " + levelAfter + " (HP & Energy refilled)";
        }
        if (completedChallenges != null) {
            for (int i = 0; i < completedChallenges.size(); i++) {
                Challenge c = completedChallenges.get(i);
                msg = msg + "\nChallenge complete: " + c.getName() + " (+$" + c.getRewardMoney() + " +" + c.getRewardExperience() + " XP)";
            }
        }
        if (newAchievements != null) {
            for (int i = 0; i < newAchievements.size(); i++) {
                Achievement a = newAchievements.get(i);
                msg = msg + "\nAchievement unlocked: " + a.getName() + " (+$" + a.getRewardMoney() + ")";
            }
        }
        return msg;
    }

    // Appends the mini-game summary.
    public static String successWithGameLine(String baseMessage, MiniGameResult game) {
        if (game == null) {
            return baseMessage;
        }
        String gameLine = GameSummaryText.summaryLine(game.getGameName(), game.getScore(),
                game.getAccuracy(), game.getElapsedSeconds(), game.getHighlights());
        return baseMessage + "\n" + gameLine;
    }

    // Builds the failure message.
    public static String failureLine(Job job, String reason) {
        String name = "Job";
        if (job != null) {
            name = job.getName();
        }
        return name + ": " + reason;
    }

    // Builds the failed-attempt message.
    public static String failedAttemptLine(Job job, int score, int energyLost, int repLost,
                                           MiniGameResult game) {
        String msg = job.getName() + " failed! Score " + score + "/100 (need " + MiniGameResult.PASS_SCORE + "+). Energy -" + energyLost;
        if (repLost > 0) {
            msg = msg + ", Reputation -" + repLost + " (botched job)";
        }
        msg = msg + ". No pay, XP or progress. Rest up and retry.";
        if (game != null) {
            msg = msg + "\n" + GameSummaryText.summaryLine(game.getGameName(), game.getScore(),
                    game.getAccuracy(), game.getElapsedSeconds(), game.getHighlights());
        }
        return msg;
    }
}
