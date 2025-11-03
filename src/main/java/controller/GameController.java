package controller;

/**
 * Central game controller coordinating sub-controllers and high-level game actions.
 *
 * Follows Single Responsibility Principle by coordinating game flow without mixing UI or persistence.
 */

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
        // construct the main game controller and its sub-controllers
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
        // purchase a service via the economy controller
        boolean result = economyController.purchaseService(serviceName);
        if (result) {
            return new ActionResult(true, "Service purchased successfully.");
        } else {
            return new ActionResult(false, "Failed to purchase service.");
        }
    }

    public ActionResult purchaseItem(Item item) {
        // purchase an item via the economy controller
        boolean result = economyController.purchaseItem(item);
        if (result) {
            return new ActionResult(true, "Item purchased successfully.");
        } else {
            return new ActionResult(false, "Failed to purchase item.");
        }
    }

    public ActionResult doWork() {
        // Show available jobs when the user clicks the Workshop in the world view.
        // If there are jobs available for the current user, the UI expects the
        // special message "JOB_DIALOG_REQUIRED" so it can open the JobDialog.
        try {
            java.util.List<Job> availableJobs = provider.JobProvider.getAvailableJobsForUser(userController.getUser());
            if (availableJobs != null && !availableJobs.isEmpty()) {
                return new ActionResult(true, "JOB_DIALOG_REQUIRED");
            } else {
                return new ActionResult(false, "No jobs are available for your current identity/skills.");
            }
        } catch (Exception ex) {
            // In case of any unexpected error, return a friendly message and log the error.
            ex.printStackTrace();
            return new ActionResult(false, "Error checking available jobs.");
        }
    }

    public ActionResult doJob(Job job) {
        // perform a job via JobController and update session stats
        boolean result = jobController.doJob(job);
        if (result) {
            sessionJobsCompleted++;
            return new ActionResult(true, "Job completed successfully.");
        } else {
            return new ActionResult(false, "Failed to complete job.");
        }
    }

    public ActionResult saveGame() {
        // save current user game state
        boolean success = saveController.saveGame(userController.getUser(), sessionStartTime);
        if (success) {
            return new ActionResult(true, "Game saved successfully!");
        } else {
            return new ActionResult(false, "Error: Could not save game data.");
        }
    }

    public ActionResult learnSkill(String skillName) {
        // learn a skill via the skill controller
        boolean result = skillController.learnSkill(skillName);
        if (result) {
            return new ActionResult(true, "Skill learned successfully.");
        } else {
            return new ActionResult(false, "Failed to learn skill.");
        }
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
