package controller;

import model.career.Job;
import model.minigame.MiniGameResult;
import model.player.User;
import model.goals.Achievement;
import model.goals.Challenge;
import model.store.JobResult;
import model.store.Service;
import provider.JobProvider;
import provider.SkillProvider;
import services.AchievementService;
import services.ChallengeService;
import java.util.List;

// Connects the small controllers and runs high-level actions like jobs and skills.
public class GameController {
    private UserController userController;
    private EconomyController economyController;
    private JobController jobController;
    private SkillController skillController;
    private StatsController statsController;
    private ChallengeService challengeService;
    private AchievementService achievementService;
    private JobProvider jobProvider;
    private SkillProvider skillProvider;
    private final long sessionStartTime;
    private int sessionJobsCompleted = 0;

    // ----- Construction -----
    public GameController(User user, long sessionStartTime) {
        this(new UserController(user), sessionStartTime);
    }

    // Default sub-controllers around one UserController.
    public GameController(UserController userController, long sessionStartTime) {
        this(userController,
                new EconomyController(userController),
                new JobController(userController),
                new SkillController(userController),
                new StatsController(),
                sessionStartTime);
    }

    // Collaborators come from outside.
    public GameController(UserController userController,
                          EconomyController economyController,
                          JobController jobController,
                          SkillController skillController,
                          StatsController statsController,
                          long sessionStartTime) {
        this(userController, economyController, jobController, skillController,
             statsController,
             new ChallengeService(), new AchievementService(), sessionStartTime);
    }

    // Services come from outside.
    public GameController(UserController userController,
                          EconomyController economyController,
                          JobController jobController,
                          SkillController skillController,
                          StatsController statsController,
                          ChallengeService challengeService,
                          AchievementService achievementService,
                          long sessionStartTime) {
        this(userController, economyController, jobController, skillController,
             statsController, challengeService, achievementService,
             new JobProvider(), new SkillProvider(), sessionStartTime);
    }

    // Catalogs come from outside.
    public GameController(UserController userController,
                          EconomyController economyController,
                          JobController jobController,
                          SkillController skillController,
                          StatsController statsController,
                          ChallengeService challengeService,
                          AchievementService achievementService,
                          JobProvider jobProvider,
                          SkillProvider skillProvider,
                          long sessionStartTime) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        if (economyController == null) {
            throw new IllegalArgumentException("economyController must not be null");
        }
        if (jobController == null) {
            throw new IllegalArgumentException("jobController must not be null");
        }
        if (skillController == null) {
            throw new IllegalArgumentException("skillController must not be null");
        }
        if (statsController == null) {
            throw new IllegalArgumentException("statsController must not be null");
        }
        this.sessionStartTime = sessionStartTime;
        this.userController = userController;
        this.economyController = economyController;
        this.jobController = jobController;
        this.skillController = skillController;
        this.statsController = statsController;
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
        if (jobProvider != null) {
            this.jobProvider = jobProvider;
        } else {
            this.jobProvider = new JobProvider();
        }
        if (skillProvider != null) {
            this.skillProvider = skillProvider;
        } else {
            this.skillProvider = new SkillProvider();
        }
        // New users start with challenges.
        this.challengeService.initializeChallenges(userController.getUser());
    }

    // ----- Commands -----
    // Buys a service with an instant effect.
    public ActionResult purchaseService(String serviceName) {
        Service service = economyController.purchaseService(serviceName);
        if (service != null) {
            String effect = utils.EffectText.formatServiceEffect(service.getEnergyEffect(), service.getHealthEffect());
            return new ActionResult(true, "Service " + serviceName + " purchased. Effect: " + effect + ".");
        } else {
            return new ActionResult(false, "Failed to purchase " + serviceName + ". Not enough balance ($" + userController.getBalance() + ").");
        }
    }

    // ----- Catalog queries -----
    public List<Service> getHealthServices() {
        return economyController.getHealthServices();
    }

    public List<Service> getEnergyServices() {
        return economyController.getEnergyServices();
    }

    // Opens the job dialog when jobs exist.
    public ActionResult doWork() {
        try {
            List<Job> allJobs = jobProvider.getAllJobs();
            if (allJobs != null && !allJobs.isEmpty()) {
                return new ActionResult(true, "", NextAction.OPEN_JOB_DIALOG);
            } else {
                return new ActionResult(false, "No jobs are available for your current identity/skills.");
            }
        } catch (Exception ex) {
            utils.GameLog.error("checking available jobs", ex);
            return new ActionResult(false, "Error checking available jobs.");
        }
    }

    public List<Job> getJobsForCurrentIdentity() {
        return jobProvider.getJobsForIdentity(userController.getIdentity());
    }

    public List<model.career.LearnableSkill> getSkillsForCurrentIdentity() {
        return skillProvider.getSkillsForIdentity(userController.getIdentity());
    }

    public ActionResult doJob(Job job) {
        JobResult r = jobController.doJob(job);
        if (r.isSuccess()) {
            sessionJobsCompleted++;
            return new ActionResult(true, r.getMessage());
        } else {
            return new ActionResult(false, r.getMessage());
        }
    }

    public ActionResult doJob(Job job, MiniGameResult game) {
        JobResult r = jobController.doJob(job, game);
        if (r.isSuccess()) {
            sessionJobsCompleted++;
            return new ActionResult(true, r.getMessage());
        } else {
            return new ActionResult(false, r.getMessage());
        }
    }

    public ActionResult learnSkill(String skillName) {
        boolean ok = skillController.learnSkill(skillName);
        if (ok) {
            return new ActionResult(true, "Skill learned successfully.");
        } else {
            return new ActionResult(false, "Failed to learn skill.");
        }
    }

    public UserController getUserController() {
        return userController;
    }

    public String getDetailedStats() {
        return statsController.getDetailedStats(userController.getUser());
    }

    public List<Challenge> getActiveChallenges() {
        return challengeService.getActiveIncompleteChallenges(userController.getUser());
    }

    public List<Challenge> getCompletedChallenges() {
        return challengeService.getCompletedChallenges(userController.getUser());
    }

    public List<Achievement> getAchievementsWithStatus() {
        return achievementService.getAchievementsWithStatus(userController.getUser());
    }

    // ----- Session queries -----
    // Play time in minutes plus jobs done this session.
    public String getSessionStats() {
        long now = System.currentTimeMillis();
        long minutes = (now - sessionStartTime) / (1000 * 60);
        return "Session: " + minutes + " minutes | Jobs: " + sessionJobsCompleted;
    }

    // What the view should do next.
    public enum NextAction {
        NONE,
        OPEN_JOB_DIALOG
    }

    // Success + message + next action.
    public static class ActionResult {
        private final boolean success;
        private final String message;
        private final NextAction nextAction;
        public ActionResult(boolean success, String message) {
            this(success, message, NextAction.NONE);
        }
        public ActionResult(boolean success, String message, NextAction nextAction) {
            this.success = success;
            this.message = message;
            if (nextAction == null) {
                this.nextAction = NextAction.NONE;
            } else {
                this.nextAction = nextAction;
            }
        }
        public boolean isSuccess() {
            return success;
        }
        public String getMessage() {
            return message;
        }
        public NextAction getNextAction() {
            return nextAction;
        }
    }
}
