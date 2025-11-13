package controller;

import model.Achievement;
import model.Challenge;
import model.GameEvent;
import model.Item;
import model.Job;
import model.User;
import model.Service;
import model.minigame.MiniGameResult;
import provider.JobProvider;
import provider.ServiceProvider;
import services.AchievementService;
import services.ChallengeService;
import java.util.List;

// Main controller that connects all small controllers.
// It shows Composition: GameController HAS-A UserController, JobController, etc.
public class GameController {
    private UserController userController;
    private EconomyController economyController;
    private JobController jobController;
    private SkillController skillController;
    private StatsController statsController;
    private EventController eventController;
    private long sessionStartTime;
    private int sessionJobsCompleted = 0;

    // Constructor: create all sub-controllers
    public GameController(User user, long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
        this.userController = new UserController(user);
        this.economyController = new EconomyController(userController);
        this.jobController = new JobController(userController);
        this.skillController = new SkillController(userController);
        this.statsController = new StatsController();
        this.eventController = new EventController();
        // make sure new users have challenges
        ChallengeService.getInstance().initializeChallenges(user);
    }

    // Buy a service (instant effect)
    public ActionResult purchaseService(String serviceName) {
        boolean ok = economyController.purchaseService(serviceName);
        Service service = ServiceProvider.getService(serviceName);
        String effect = "";
        if (service != null) {
            effect = service.getEffectDescription();
        }
        if (ok) {
            return new ActionResult(true, "Service " + serviceName + " purchased. Effect: " + effect + ".");
        } else {
            return new ActionResult(false, "Failed to purchase " + serviceName + ". Not enough balance ($" + userController.getBalance() + ").");
        }
    }

    // Buy an item (goes to inventory)
    public ActionResult purchaseItem(Item item) {
        if (item == null) {
            return new ActionResult(false, "No item selected.");
        }
        boolean ok = economyController.purchaseItem(item);
        if (ok) {
            int owned = userController.getUser().getInventory().countByName(item.getName());
            String life = "single-use";
            if (!item.isConsumable()) {
                life = "reusable";
            }
            String msg = "Bought " + item.getName() + " (-$" + item.getPrice() + "). Effect: " + item.getEffectDescription();
            msg = msg + " (" + life + "). Owned: " + owned + ". Open inventory to use it.";
            return new ActionResult(true, msg);
        } else {
            return new ActionResult(false, "Failed to purchase " + item.getName() + " ($" + item.getPrice() + "). Balance ($" + userController.getBalance() + ").");
        }
    }

    // Use an item from inventory
    public ActionResult useItem(String itemName) {
        if (itemName == null || itemName.trim().equals("")) {
            return new ActionResult(false, "No item selected.");
        }
        String effect = economyController.useItem(itemName);
        if (effect != null) {
            return new ActionResult(true, "Used " + itemName + ": " + effect + ".");
        }
        return new ActionResult(false, "Item not in inventory: " + itemName);
    }

    // Return inventory items
    public List<Item> getInventoryItems() {
        return userController.getUser().getInventory().getItems();
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

    // Do one job
    public ActionResult doJob(Job job) {
        model.JobResult r = jobController.doJob(job);
        if (r.isSuccess()) {
            sessionJobsCompleted++;
            return new ActionResult(true, r.getMessage());
        } else {
            return new ActionResult(false, r.getMessage());
        }
    }

    // Do one job with mini-game
    public ActionResult doJob(Job job, MiniGameResult game) {
        model.JobResult r = jobController.doJob(job, game);
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

    // Learn a skill
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
        return ChallengeService.getInstance().getActiveIncompleteChallenges(userController.getUser());
    }

    public List<Challenge> getCompletedChallenges() {
        return ChallengeService.getInstance().getCompletedChallenges(userController.getUser());
    }

    public List<Achievement> getAchievementsWithStatus() {
        return AchievementService.getInstance().getAchievementsWithStatus(userController.getUser());
    }

    public GameEvent triggerRandomEvent() {
        return eventController.triggerRandomEvent(userController.getUser());
    }

    // Simple session info, no String.format
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
