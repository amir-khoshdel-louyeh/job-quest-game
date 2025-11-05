package controller;

/**
 * Controller handling job-related game actions such as performing jobs and awarding rewards.
 *
 * Follows Single Responsibility Principle by managing job workflows and coordination only.
 */

import model.Job;
import model.JobResult;
import services.JobService;

public class JobController {
    private final UserController userController;
    private final JobService jobService;

    public JobController(UserController userController) {
        // default wiring: real domain service (Dependency Injection root)
        this(userController, new JobService());
    }

    /**
     * Injectable constructor for tests / OOP Dependency Inversion:
     * callers depend on the {@link JobService} abstraction, not inline math.
     */
    public JobController(UserController userController, JobService jobService) {
        // construct job controller with a user controller and job service
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        this.userController = userController;
        this.jobService = (jobService != null) ? jobService : new JobService();
    }

    /**
     * Perform a job: validate → apply costs/rewards via controllers only
     * (keeps {@link model.User} encapsulated) → return rich {@link JobResult}.
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
        return JobResult.success(job, base, bonus, finalPay, xp, rep, levelBefore, levelAfter);
    }

    /**
     * Legacy boolean API kept for backward compatibility (Open/Closed):
     * new code should use {@link #doJob(Job)} returning {@link JobResult}.
     */
    public boolean doJobLegacy(Job job) {
        return doJob(job).isSuccess();
    }
}
