package controller;

import model.*;
public class GameController {
    private final UserController userController;
    private final EconomyController economyController;
    private final JobController jobController;
    private final SkillController skillController;
    private final SaveController saveController;
    private final StatsController statsController;
    private final EventController eventController;
    private final long sessionStartTime;
    private int sessionJobsCompleted = 0;

    public GameController(User user, long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
        this.userController = new UserController(user);
        this.economyController = new EconomyController(userController);
        this.jobController = new JobController(userController);
        this.skillController = new SkillController(userController);
        this.saveController = new SaveController();
        this.statsController = new StatsController();
        this.eventController = new EventController();
    }

    public ActionResult purchaseService(String serviceName) {
        boolean result = economyController.purchaseService(serviceName);
        if (result) {
            return new ActionResult(true, "Service purchased successfully.");
        } else {
            return new ActionResult(false, "Failed to purchase service.");
        }
    }

    public ActionResult purchaseItem(Item item) {
        boolean result = economyController.purchaseItem(item);
        if (result) {
            return new ActionResult(true, "Item purchased successfully.");
        } else {
            return new ActionResult(false, "Failed to purchase item.");
        }
    }

    public ActionResult doWork() {
        // This would be handled by JobController in a real refactor
        return new ActionResult(false, "Not implemented: doWork");
    }

    public ActionResult doJob(Job job) {
        boolean result = jobController.doJob(job);
        if (result) {
            sessionJobsCompleted++;
            return new ActionResult(true, "Job completed successfully.");
        } else {
            return new ActionResult(false, "Failed to complete job.");
        }
    }

    public ActionResult saveGame() {
        boolean success = saveController.saveGame(userController.getUser(), sessionStartTime);
        if (success) {
            return new ActionResult(true, "Game saved successfully!");
        } else {
            return new ActionResult(false, "Error: Could not save game data.");
        }
    }

    public ActionResult learnSkill(String skillName) {
        boolean result = skillController.learnSkill(skillName);
        if (result) {
            return new ActionResult(true, "Skill learned successfully.");
        } else {
            return new ActionResult(false, "Failed to learn skill.");
        }
    }

    public ActionResult completeTask(Task task) {
        // This would be handled by JobController or TaskController in a real refactor
        return new ActionResult(false, "Not implemented: completeTask");
    }

    public UserController getUserController() {
        return userController;
    }

    public String getDetailedStats() {
        return statsController.getDetailedStats(userController.getUser());
    }

    // For brevity, getActiveChallenges, getAchievements, etc. would be delegated to their own controllers/services.

    public GameEvent triggerRandomEvent() {
        return eventController.triggerRandomEvent(userController.getUser());
    }

    public String getSessionStats() {
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        long minutes = sessionDuration / (1000 * 60);
        return String.format("Session: %d minutes | Jobs: %d", minutes, sessionJobsCompleted);
    }

    public static class ActionResult {
        public final boolean success;
        public final String message;
        public ActionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
