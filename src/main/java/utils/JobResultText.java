package utils;

import java.util.List;

import model.career.Job;
import model.goals.Achievement;
import model.goals.Challenge;
import model.minigame.MiniGameResult;

// Single home for job-result message texts (same role as GameSummaryText
// for mini-games). Keeps string building out of the JobResult DTO (SRP):
// the model holds data, this owns the wording.
public final class JobResultText {
    private JobResultText() {
    }

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

    public static String successWithGameLine(String baseMessage, MiniGameResult game) {
        if (game == null) {
            return baseMessage;
        }
        String gameLine = GameSummaryText.summaryLine(game.getGameName(), game.getScore(),
                game.getAccuracy(), game.getWpm(), game.getElapsedSeconds(), game.getHighlights());
        return baseMessage + "\n" + gameLine;
    }

    public static String failureLine(Job job, String reason) {
        String name = "Job";
        if (job != null) {
            name = job.getName();
        }
        return name + ": " + reason;
    }

    public static String failedAttemptLine(Job job, int score, int energyLost, int repLost,
                                           MiniGameResult game) {
        String msg = job.getName() + " failed! Score " + score + "/100 (need " + MiniGameResult.PASS_SCORE + "+). Energy -" + energyLost;
        if (repLost > 0) {
            msg = msg + ", Reputation -" + repLost + " (botched job)";
        }
        msg = msg + ". No pay, XP or progress. Rest up and retry.";
        if (game != null) {
            msg = msg + "\n" + GameSummaryText.summaryLine(game.getGameName(), game.getScore(),
                    game.getAccuracy(), game.getWpm(), game.getElapsedSeconds(), game.getHighlights());
        }
        return msg;
    }
}
