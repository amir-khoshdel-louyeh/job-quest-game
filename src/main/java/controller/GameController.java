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
        this.sessionStartTime = sessionStartTime;
        this.userController = new UserController(user);
        this.economyController = new EconomyController(userController);
        this.jobController = new JobController(userController);
        this.skillController = new SkillController(userController);
        this.statsController = new StatsController();
        this.eventController = new EventController();
        // make sure new users have challenges
        ChallengeService.initializeChallenges(user);
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
                return new ActionResult(true, "JOB_DIALOG_REQUIRED");
            } else {
                return new ActionResult(false, "No jobs are available for your current identity/skills.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
    public ActionResult saveGame() {
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        userController.getUser().setTotalPlayTime(
            userController.getUser().getTotalPlayTime() + sessionDuration);
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

    // Small helper class to return success + message
    public static class ActionResult {
        public boolean success;
        public String message;
        public ActionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
