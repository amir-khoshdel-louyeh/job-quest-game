package controller;

import model.career.Job;
import model.minigame.MiniGameResult;
import model.player.User;
import model.goals.Achievement;
import model.goals.Challenge;
import model.store.JobResult;
import services.AchievementService;
import services.ChallengeService;
import services.JobService;
import java.util.ArrayList;
import java.util.List;

// Runs one job: checks energy, rolls payment, hands out XP.
public class JobController {
    private UserController userController;
    private JobService jobService;

    public JobController(UserController userController) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        this.userController = userController;
        this.jobService = new JobService();
    }

    // Constructor for tests (can pass own JobService)
    public JobController(UserController userController, JobService jobService) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        this.userController = userController;
        if (jobService != null) {
            this.jobService = jobService;
        } else {
            this.jobService = new JobService();
        }
    }

    // Do a job without mini-game
    public JobResult doJob(Job job) {
        return execute(job, null);
    }

    // Do a job with mini-game result
    public JobResult doJob(Job job, MiniGameResult game) {
        return execute(job, game);
    }

    private JobResult execute(Job job, MiniGameResult game) {
        if (job == null) {
            return JobResult.failure(null, "No job selected.");
        }
        if (userController.isBlocked()) {
            return JobResult.failure(job, "You are blocked. Rest and try later.");
        }
        // Check identity + skills via controller delegates (Law of Demeter:
        // no getUser().getIdentity()/getSkills() train-wreck here).
        if (!job.isAvailableFor(userController.getIdentity(), userController.getSkills())) {
            return JobResult.failure(job, "Not available for your identity/skills. Learn more skills!");
        }
        // Check energy
        if (userController.getEnergy() < job.getEnergyCost()) {
            String msg = "Not enough energy (need " + job.getEnergyCost() + ", have " + userController.getEnergy() + "). Eat or rest first.";
            return JobResult.failure(job, msg);
        }

        // If mini-game failed (score < 50), lose energy but get nothing
        if (game != null && !game.isPassed()) {
            userController.decreaseEnergy(job.getEnergyCost());
            int repLoss = 0;
            if (game.getScore() == 0) {
                userController.addReputation(-1);
                repLoss = 1;
            }
            return JobResult.failedAttempt(job, game.getScore(), job.getEnergyCost(), repLoss, game);
        }

        // Calculate rewards
        User user = userController.getUser();
        int base = job.getPayment();
        int scaledBase = base;
        int xp = jobService.calculateXpReward(job);
        int rep = jobService.calculateReputationGain(job);
        if (game != null) {
            scaledBase = (int) Math.round(base * game.rewardMultiplier());
            if (scaledBase < 1) {
                scaledBase = 1;
            }
            xp = (int) Math.round(xp * game.rewardMultiplier());
            if (xp < 1) {
                xp = 1;
            }
            if (game.getScore() >= 80) {
                rep = rep + 1;
            }
        }
        int finalPay = jobService.calculateFinalPayment(job, user);
        if (game != null && base > 0) {
            double ratio = scaledBase / (double) base;
            finalPay = (int) Math.round(finalPay * ratio);
            if (finalPay < 1) {
                finalPay = 1;
            }
        }
        int bonus = finalPay - base;
        if (bonus < 0) {
            bonus = 0;
        }
        int levelBefore = user.getLevel();

        // Hand out the rewards.
        userController.decreaseEnergy(job.getEnergyCost());
        userController.addBalance(finalPay);
        userController.addExperience(xp);
        userController.addReputation(rep);
        userController.recordJobCompletion(finalPay);

        int levelAfter = userController.getLevel();

        // Update challenges
        if (user.getActiveChallenges().isEmpty()) {
            ChallengeService.initializeChallenges(user);
        }
        ArrayList<Challenge> completed = new ArrayList<Challenge>();
        List<Challenge> c1 = ChallengeService.updateChallengeProgress(user, Challenge.ChallengeType.COMPLETE_JOBS, 1);
        for (int i = 0; i < c1.size(); i++) {
            completed.add(c1.get(i));
        }
        List<Challenge> c2 = ChallengeService.updateChallengeProgress(user, Challenge.ChallengeType.EARN_MONEY, finalPay);
        for (int i = 0; i < c2.size(); i++) {
            completed.add(c2.get(i));
        }
        List<Challenge> c3 = ChallengeService.updateChallengeProgress(user, Challenge.ChallengeType.SPEND_ENERGY, job.getEnergyCost());
        for (int i = 0; i < c3.size(); i++) {
            completed.add(c3.get(i));
        }

        List<Achievement> unlocked = AchievementService.checkAndUnlockAchievements(user);

        userController.refresh();

        if (game != null) {
            return JobResult.success(job, base, bonus, finalPay, xp, rep, levelBefore, levelAfter, completed, unlocked, game);
        }
        return JobResult.success(job, base, bonus, finalPay, xp, rep, levelBefore, levelAfter, completed, unlocked);
    }
}
