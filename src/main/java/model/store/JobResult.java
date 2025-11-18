package model.store;

import java.util.ArrayList;
import java.util.List;

import model.career.Job;
import model.goals.Achievement;
import model.goals.Challenge;

// Holds the outcome and rewards of doing a job.
public final class JobResult {
    private boolean success;
    private String message;
    private Job job;
    private int basePayment;
    private int bonusAmount;
    private int finalPayment;
    private int xpGained;
    private int reputationGained;
    private int levelBefore;
    private int levelAfter;
    private ArrayList<Challenge> completedChallenges;
    private ArrayList<Achievement> newAchievements;

    private JobResult(boolean success, String message, Job job,
                      int basePayment, int bonusAmount, int finalPayment,
                      int xpGained, int reputationGained,
                      int levelBefore, int levelAfter,
                      List<Challenge> completedChallenges,
                      List<Achievement> newAchievements) {
        this.success = success;
        this.message = message;
        this.job = job;
        this.basePayment = basePayment;
        this.bonusAmount = bonusAmount;
        this.finalPayment = finalPayment;
        this.xpGained = xpGained;
        this.reputationGained = reputationGained;
        this.levelBefore = levelBefore;
        this.levelAfter = levelAfter;
        this.completedChallenges = new ArrayList<Challenge>();
        if (completedChallenges != null) {
            for (int i = 0; i < completedChallenges.size(); i++) {
                this.completedChallenges.add(completedChallenges.get(i));
            }
        }
        this.newAchievements = new ArrayList<Achievement>();
        if (newAchievements != null) {
            for (int i = 0; i < newAchievements.size(); i++) {
                this.newAchievements.add(newAchievements.get(i));
            }
        }
    }

    // Success data; the message wording lives in utils.JobResultText (SRP).
    public static JobResult success(Job job, int basePayment, int bonusAmount, int finalPayment,
                                    int xpGained, int reputationGained,
                                    int levelBefore, int levelAfter,
                                    List<Challenge> completedChallenges,
                                    List<Achievement> newAchievements) {
        String msg = utils.JobResultText.successLine(job, basePayment, bonusAmount, finalPayment,
                xpGained, reputationGained, levelBefore, levelAfter, completedChallenges, newAchievements);
        return new JobResult(true, msg, job, basePayment, bonusAmount, finalPayment,
                xpGained, reputationGained, levelBefore, levelAfter, completedChallenges, newAchievements);
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
        return new JobResult(true, withGame, job, basePayment, bonusAmount, finalPayment,
                xpGained, reputationGained, levelBefore, levelAfter, completedChallenges, newAchievements);
    }

    public static JobResult failure(Job job, String reason) {
        String msg = utils.JobResultText.failureLine(job, reason);
        ArrayList<Challenge> empty1 = new ArrayList<Challenge>();
        ArrayList<Achievement> empty2 = new ArrayList<Achievement>();
        return new JobResult(false, msg, job, 0, 0, 0, 0, 0, 0, 0, empty1, empty2);
    }

    // Failed-attempt data; the message wording lives in utils.JobResultText (SRP).
    public static JobResult failedAttempt(Job job, int score, int energyLost, int repLost,
                                          model.minigame.MiniGameResult game) {
        String msg = utils.JobResultText.failedAttemptLine(job, score, energyLost, repLost, game);
        ArrayList<Challenge> empty1 = new ArrayList<Challenge>();
        ArrayList<Achievement> empty2 = new ArrayList<Achievement>();
        return new JobResult(false, msg, job, 0, 0, 0, 0, 0, 0, 0, empty1, empty2);
    }

    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public Job getJob() {
        return job;
    }
    public int getBasePayment() {
        return basePayment;
    }
    public int getBonusAmount() {
        return bonusAmount;
    }
    public int getFinalPayment() {
        return finalPayment;
    }
    public int getXpGained() {
        return xpGained;
    }
    public int getReputationGained() {
        return reputationGained;
    }
    public int getLevelBefore() {
        return levelBefore;
    }
    public int getLevelAfter() {
        return levelAfter;
    }
    // True when the level after the job is higher than before.
    public boolean didLevelUp() {
        return levelAfter > levelBefore;
    }
    public List<Challenge> getCompletedChallenges() {
        ArrayList<Challenge> copy = new ArrayList<Challenge>();
        for (int i = 0; i < completedChallenges.size(); i++) {
            copy.add(completedChallenges.get(i));
        }
        return copy;
    }
    public List<Achievement> getNewAchievements() {
        ArrayList<Achievement> copy = new ArrayList<Achievement>();
        for (int i = 0; i < newAchievements.size(); i++) {
            copy.add(newAchievements.get(i));
        }
        return copy;
    }
}
