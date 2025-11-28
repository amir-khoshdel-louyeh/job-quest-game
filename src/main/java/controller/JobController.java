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
    private ChallengeService challengeService;
    private AchievementService achievementService;

    // ----- Construction -----
    public JobController(UserController userController) {
        this(userController, new JobService(), new ChallengeService(), new AchievementService());
    }

    // Custom JobService for tests.
    public JobController(UserController userController, JobService jobService) {
        this(userController, jobService, new ChallengeService(), new AchievementService());
    }

    // Services come from outside.
    public JobController(UserController userController, JobService jobService,
                         ChallengeService challengeService, AchievementService achievementService) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        this.userController = userController;
        if (jobService != null) {
            this.jobService = jobService;
        } else {
            this.jobService = new JobService();
        }
        if (challengeService != null) {
            this.challengeService = challengeService;
        } else {
            this.challengeService = new ChallengeService();
        }
        if (achievementService != null) {
            this.achievementService = achievementService;
        } else {
            this.achievementService = new AchievementService();
        }
    }

    // ----- Commands -----
    // Runs a job without a mini-game.
    public JobResult doJob(Job job) {
        return execute(job, null);
    }

    // Runs a job with a mini-game result.
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
        // Identity + skills gate the job.
        if (!job.isAvailableFor(userController.getIdentity(), userController.getSkillSet())) {
            return JobResult.failure(job, "Not available for your identity/skills. Learn more skills!");
        }
        // Energy gate.
        if (userController.getEnergy() < job.getEnergyCost()) {
            String msg = "Not enough energy (need " + job.getEnergyCost() + ", have " + userController.getEnergy() + "). Eat or rest first.";
            return JobResult.failure(job, msg);
        }

        // Failed mini-game: lose energy, gain nothing.
        if (game != null && !game.isPassed()) {
            userController.decreaseEnergy(job.getEnergyCost());
            int repLoss = 0;
            if (game.getScore() == 0) {
                userController.loseReputation(2);
                repLoss = 2;
            } else {
                userController.loseReputation(1);
                repLoss = 1;
            }
            return JobResult.failedAttempt(job, game.getScore(), job.getEnergyCost(), repLoss, game);
        }

        // Rewards math lives in JobService.
        User user = userController.getUser();
        int base = job.getPayment();
        int scaledBase = jobService.scalePayment(base, game);
        int xp = jobService.scaleXp(jobService.calculateXpReward(job), game);
        int rep = jobService.calculateReputationGain(job) + jobService.extraReputation(game);
        int finalPay = jobService.applyGameRatio(
                jobService.calculateFinalPayment(job, user), scaledBase, base, game);
        int bonus = jobService.bonusAmount(finalPay, base);
        int levelBefore = user.getLevel();

        // Hand out the rewards in one call each.
        userController.decreaseEnergy(job.getEnergyCost());
        userController.recordJobCompletion(finalPay);
        userController.addExperience(xp);
        userController.gainReputation(rep);

        int levelAfter = userController.getLevel();

        // Update challenges and achievements.
        if (userController.getActiveChallenges().isEmpty()) {
            challengeService.initializeChallenges(user);
        }
        ArrayList<Challenge> completed = new ArrayList<Challenge>();
        collectCompleted(completed,
                challengeService.updateChallengeProgress(user, Challenge.ChallengeType.COMPLETE_JOBS, 1));
        collectCompleted(completed,
                challengeService.updateChallengeProgress(user, Challenge.ChallengeType.EARN_MONEY, finalPay));

        List<Achievement> unlocked = achievementService.checkAndUnlockAchievements(user);

        userController.refresh();

        if (game != null) {
            return JobResult.success(job, base, bonus, finalPay, xp, rep, levelBefore, levelAfter, completed, unlocked, game);
        }
        return JobResult.success(job, base, bonus, finalPay, xp, rep, levelBefore, levelAfter, completed, unlocked);
    }

    // ----- Private helpers -----
    private static void collectCompleted(ArrayList<Challenge> into, List<Challenge> part) {
        for (int i = 0; i < part.size(); i++) {
            into.add(part.get(i));
        }
    }
}
