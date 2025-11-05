package controller;

/**
 * Controller handling job-related game actions such as performing jobs and awarding rewards.
 *
 * Follows Single Responsibility Principle by managing job workflows and coordination only.
 */

import model.Achievement;
import model.Challenge;
import model.Job;
import model.JobResult;
import services.AchievementService;
import services.ChallengeService;
import services.JobService;

public class JobController {
    private final UserController userController;
    private final JobService jobService;
    private final ChallengeService challengeService;
    private final AchievementService achievementService;

    public JobController(UserController userController) {
        // default wiring: real domain services incl. progression Singletons
        this(userController, new JobService(),
                ChallengeService.getInstance(), AchievementService.getInstance());
    }

    /**
     * Injectable constructor for tests / OOP Dependency Inversion:
     * callers depend on service abstractions, not inline math or statics.
     */
    public JobController(UserController userController, JobService jobService) {
        this(userController, jobService,
                ChallengeService.getInstance(), AchievementService.getInstance());
    }

    /**
     * Full injection (Singletons passable as test doubles).
     * Preserves Singleton pattern in production, DIP in tests.
     */
    public JobController(UserController userController, JobService jobService,
                         ChallengeService challengeService,
                         AchievementService achievementService) {
        // construct job controller with a user controller and services
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        this.userController = userController;
        this.jobService = (jobService != null) ? jobService : new JobService();
        this.challengeService = (challengeService != null) ? challengeService : ChallengeService.getInstance();
        this.achievementService = (achievementService != null) ? achievementService : AchievementService.getInstance();
    }

    /**
     * Perform a job: validate → apply costs/rewards via controllers only
     * (keeps {@link model.User} encapsulated) → update progression
     * (challenges + achievements via Singleton services) → rich {@link JobResult}.
     * Single Responsibility: coordination only, math lives in {@link JobService}.
     */
    public JobResult doJob(Job job) {
        // attempt to perform the given job for the user
        if (job == null) {
            return JobResult.failure(null, "No job selected.");
        }
        if (userController.isBlocked()) {
            return JobResult.failure(job, "You are blocked. Rest and try later.");
        }
        // Eligibility: identity + required skills (Polymorphism via Job#isAvailableFor).
        java.util.List<String> userSkills = userController.getUser().getSkills()
                .stream().map(model.Skill::getName).toList();
        String userIdentity = userController.getUser().getIdentity().getClass().getSimpleName();
        if (!job.isAvailableFor(userIdentity, userSkills)) {
            return JobResult.failure(job, "Not available for your identity/skills. Learn more skills!");
        }
        // Simple pre-check: ensure user has enough energy to start the job.
        if (userController.getEnergy() < job.getEnergyCost()) {
            return JobResult.failure(job,
                    String.format("Not enough energy (need %d, have %d). Eat or rest first.",
                            job.getEnergyCost(), userController.getEnergy()));
        }

        // Pure calculations first (no side effects) — easy to unit test.
        model.User user = userController.getUser();
        int base = job.getPayment();
        int finalPay = jobService.calculateFinalPayment(job, user);
        int bonus = Math.max(0, finalPay - base);
        int xp = jobService.calculateXpReward(job);
        int rep = jobService.calculateReputationGain(job);
        int levelBefore = user.getLevel();

        // Mutations go through UserController so Observers are notified (Observer pattern).
        userController.decreaseEnergy(job.getEnergyCost());
        userController.addBalance(finalPay);
        userController.addExperience(xp);
        userController.addReputation(rep);
        userController.recordJobCompletion(finalPay);

        int levelAfter = userController.getLevel();

        // ---- Progression wiring (Step 2): challenges + achievements ----
        // Defensive: ensure main/daily challenges exist even for legacy saves.
        if (user.getActiveChallenges().isEmpty()) {
            challengeService.initializeChallenges(user);
        }
        java.util.List<Challenge> completed = new java.util.ArrayList<>();
        completed.addAll(challengeService.updateChallengeProgress(
                user, Challenge.ChallengeType.COMPLETE_JOBS, 1));
        completed.addAll(challengeService.updateChallengeProgress(
                user, Challenge.ChallengeType.EARN_MONEY, finalPay));
        completed.addAll(challengeService.updateChallengeProgress(
                user, Challenge.ChallengeType.SPEND_ENERGY, job.getEnergyCost()));

        java.util.List<Achievement> unlocked =
                achievementService.checkAndUnlockAchievements(user);

        // Challenge/Achievement services mutate User directly (deposit/XP);
        // re-sync observers so HUD stays consistent.
        userController.refresh();

        return JobResult.success(job, base, bonus, finalPay, xp, rep,
                levelBefore, levelAfter, completed, unlocked);
    }

    /**
     * Legacy boolean API kept for backward compatibility (Open/Closed):
     * new code should use {@link #doJob(Job)} returning {@link JobResult}.
     */
    public boolean doJobLegacy(Job job) {
        return doJob(job).isSuccess();
    }
}
