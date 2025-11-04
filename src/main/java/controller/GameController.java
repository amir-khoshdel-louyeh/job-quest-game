package controller;

/**
 * Central game controller coordinating sub-controllers and high-level game actions.
 *
 * Follows Single Responsibility Principle by coordinating game flow without mixing UI or persistence.
 */

import controller.delegator.EconomyDelegator;
import controller.delegator.JobDelegator;
import controller.delegator.SaveDelegator;
import controller.delegator.SkillDelegator;
import model.*;

public class GameController {
    private final UserController userController;
    private final StatsController statsController;
    private final EventController eventController;
    private final EconomyDelegator economyDelegator;
    private final SkillDelegator skillDelegator;
    private final JobDelegator jobDelegator;
    private final SaveDelegator saveDelegator;
    private final long sessionStartTime;
    private int sessionJobsCompleted = 0;

    public GameController(User user, long sessionStartTime) {
        // construct the main game controller and its sub-controllers
        this.sessionStartTime = sessionStartTime;
        this.userController = new UserController(user);
        EconomyController economyHandler = new EconomyController(userController);
        JobController jobHandler = new JobController(userController);
        SkillController skillHandler = new SkillController(userController);
        SaveController saveHandler = new SaveController();
        this.statsController = new StatsController();
        this.eventController = new EventController();
        this.economyDelegator = new EconomyDelegator(economyHandler);
        this.skillDelegator = new SkillDelegator(skillHandler);
        this.jobDelegator = new JobDelegator(jobHandler, userController);
        this.saveDelegator = new SaveDelegator(saveHandler, userController);
    }

    public ActionResult purchaseService(String serviceName) {
        // purchase a service via the economy delegator
        return economyDelegator.purchaseService(serviceName);
    }

    public ActionResult purchaseItem(Item item) {
        // purchase an item via the economy delegator
        return economyDelegator.purchaseItem(item);
    }

    public ActionResult doWork() {
        // Request job availability via the job delegator
        return jobDelegator.requestJobDialog();
    }

    public ActionResult doJob(Job job) {
        // perform a job via JobDelegator and update session stats
        ActionResult result = jobDelegator.performJob(job);
        if (result.success) {
            sessionJobsCompleted++;
        }
        return result;
    }

    public ActionResult saveGame() {
        // save current user game state via delegator
        return saveDelegator.saveGame(sessionStartTime);
    }

    public ActionResult learnSkill(String skillName) {
        // learn a skill via the skill delegator
        return skillDelegator.learnSkill(skillName);
    }

    public ActionResult completeTask(Task task) {
        // placeholder for task completion (not implemented)
        // This would be handled by JobController or TaskController in a real refactor
        return new ActionResult(false, "Not implemented: completeTask");
    }

    public UserController getUserController() {
        // return the user controller instance
        return userController;
    }

    public String getDetailedStats() {
        // get a detailed stats string for the current user
        return statsController.getDetailedStats(userController.getUser());
    }

    // For brevity, getActiveChallenges, getAchievements, etc. would be delegated to their own controllers/services.

    public GameEvent triggerRandomEvent() {
        // trigger a random game event for the current user
        return eventController.triggerRandomEvent(userController.getUser());
    }

    public String getSessionStats() {
        // return a short summary of the current session stats
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        long minutes = sessionDuration / (1000 * 60);
        return String.format("Session: %d minutes | Jobs: %d", minutes, sessionJobsCompleted);
    }

    public static class ActionResult {
        public final boolean success;
        public final String message;
        public ActionResult(boolean success, String message) {
            // construct an action result reporting success and a message
            this.success = success;
            this.message = message;
        }
    }
}
