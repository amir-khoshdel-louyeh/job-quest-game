package controller;

import model.career.Job;
import model.minigame.MiniGameResult;
import model.player.User;
import model.goals.Achievement;
import model.goals.Challenge;
import model.store.GameEvent;
import model.store.JobResult;
import model.store.Service;
import provider.JobProvider;
import provider.ServiceProvider;
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
    private EventController eventController;
    private long sessionStartTime;
    private int sessionJobsCompleted = 0;

    public GameController(User user, long sessionStartTime) {
        this(new UserController(user), sessionStartTime);
    }

    // Convenience: builds the default sub-controllers around one UserController.
    public GameController(UserController userController, long sessionStartTime) {
        this(userController,
                new EconomyController(userController),
                new JobController(userController),
                new SkillController(userController),
                new StatsController(),
                new EventController(),
                sessionStartTime);
    }

    // Injection seam: collaborators come from outside (testable, swappable).
    public GameController(UserController userController,
                          EconomyController economyController,
                          JobController jobController,
                          SkillController skillController,
                          StatsController statsController,
                          EventController eventController,
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
        if (eventController == null) {
            throw new IllegalArgumentException("eventController must not be null");
        }
        this.sessionStartTime = sessionStartTime;
        this.userController = userController;
        this.economyController = economyController;
        this.jobController = jobController;
        this.skillController = skillController;
        this.statsController = statsController;
        this.eventController = eventController;
        // make sure new users have challenges
        ChallengeService.initializeChallenges(userController.getUser());
    }

    // Buy a service (instant effect)
    public ActionResult purchaseService(String serviceName) {
        boolean ok = economyController.purchaseService(serviceName);
        Service service = ServiceProvider.getService(serviceName);
        String effect = "";
        if (service != null) {
            effect = utils.EffectText.formatServiceEffect(service.getEnergyEffect(), service.getHealthEffect());
        }
        if (ok) {
            return new ActionResult(true, "Service " + serviceName + " purchased. Effect: " + effect + ".");
        } else {
            return new ActionResult(false, "Failed to purchase " + serviceName + ". Not enough balance ($" + userController.getBalance() + ").");
        }
    }

    // Check jobs (workshop always opens dialog if jobs exist)
    public ActionResult doWork() {
        try {
            List<Job> allJobs = JobProvider.getAllJobs();
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

    // No persistence - game runs in memory only. Just track play time.
    // Play time only moves forward via User.addPlayTime (encapsulation:
    // no wholesale setTotalPlayTime that could rewind the clock).
    public ActionResult saveGame() {
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        userController.addPlayTime(sessionDuration);
        sessionStartTime = System.currentTimeMillis();
        return new ActionResult(true, "Progress kept for this session (no save file).");
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
        return ChallengeService.getActiveIncompleteChallenges(userController.getUser());
    }

    public List<Challenge> getCompletedChallenges() {
        return ChallengeService.getCompletedChallenges(userController.getUser());
    }

    public List<Achievement> getAchievementsWithStatus() {
        return AchievementService.getAchievementsWithStatus(userController.getUser());
    }

    public GameEvent triggerRandomEvent() {
        return eventController.triggerRandomEvent(userController.getUser());
    }

    // Play time in minutes plus jobs done this session.
    public String getSessionStats() {
        long now = System.currentTimeMillis();
        long minutes = (now - sessionStartTime) / (1000 * 60);
        return "Session: " + minutes + " minutes | Jobs: " + sessionJobsCompleted;
    }

    // What the view should do next. Replaces the old "JOB_DIALOG_REQUIRED"
    // magic message: control flow is typed, display text stays in message.
    public enum NextAction {
        NONE,
        OPEN_JOB_DIALOG
    }

    // Small immutable helper class to return success + message + next action.
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
