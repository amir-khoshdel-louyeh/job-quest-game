package controller;

import database.DatabaseUtil;
import model.WorkResult;
import model.Service;
import model.ServiceProvider;
import model.LearnableSkill;
import model.SkillProvider;
import model.Skill;
import model.Job;
import model.Item;
import model.User;

public class GameController {
    private UserController userController;
    private final long sessionStartTime;

    public GameController(User user, long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
        this.userController = new UserController(user);
    }

    public ActionResult purchaseService(String serviceName) {
        Service service = ServiceProvider.getService(serviceName);
        if (service == null) {
            return new ActionResult(false, "Service '" + serviceName + "' not found.");
        }

        if (userController.deductBalance(service.getCost())) {
            userController.increaseEnergy(service.getEnergyEffect());
            userController.increaseHealth(service.getHealthEffect());
            userController.notifyObservers(); // This will update the UI stats
            String message = String.format("%s used %s. Cost: $%d.",
                    userController.getUsername(),
                    service.getName(),
                    service.getCost());
            return new ActionResult(true, message);
        } else {
            return new ActionResult(false, "Not enough money for " + service.getName() + "!");
        }
    }

    public ActionResult purchaseItem(Item item) {
        if (item == null) {
            return new ActionResult(false, "Item not found.");
        }

        if (userController.deductBalance(item.getPrice())) {
            userController.addItemToInventory(item);
            userController.notifyObservers(); // Update UI stats
            String message = String.format("🛒 You bought %s for $%d.",
                    item.getName(),
                    item.getPrice());
            return new ActionResult(true, message);
        } else {
            return new ActionResult(false, "Not enough money for " + item.getName() + "!");
        }
    }

    public ActionResult doWork() {
        WorkResult workResult = userController.getUser().getIdentity().performDailyWork();
        // Since all identities now use the job dialog, this is the only path.
        if (workResult.getType() == WorkResult.Type.REQUIRES_DIALOG) {
            return new ActionResult(false, "JOB_DIALOG_REQUIRED"); // Special message for the view
        }
        // The old instant-earning logic is no longer reachable and has been removed.
        return new ActionResult(false, "No work available."); // Fallback
    }

    public ActionResult doJob(Job job) {
        if (userController.getEnergy() >= job.getEnergyCost()) {
            userController.addBalance(job.getPayment());
            userController.decreaseEnergy(job.getEnergyCost());
            userController.notifyObservers();
            String message = String.format("Completed '%s'. Earned $%d, used %d energy.",
                    job.getName(),
                    job.getPayment(),
                    job.getEnergyCost());
            return new ActionResult(true, message);
        } else {
            return new ActionResult(false, "Not enough energy to do this job!");
        }
    }

    public ActionResult saveGame() {
        // Update total play time before saving
        User user = userController.getUser();
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        long newTotalPlayTime = user.getTotalPlayTime() + sessionDuration;
        user.setTotalPlayTime(newTotalPlayTime);

        boolean success = DatabaseUtil.updateUser(user);
        if (success) {
            return new ActionResult(true, "Game saved successfully!");
        } else {
            return new ActionResult(false, "Error: Could not save game data.");
        }
    }

    public ActionResult learnSkill(String skillName) {
        LearnableSkill skillToLearn = SkillProvider.getSkill(skillName);
        if (skillToLearn == null) {
            return new ActionResult(false, "Skill '" + skillName + "' not found.");
        }

        // Check if user has enough money
        if (userController.deductBalance(skillToLearn.getCost())) {
            // Add the skill to the user
            userController.getUser().getSkills().add(new Skill(skillToLearn.getName()));
            userController.notifyObservers(); // Update UI

            String message = String.format("🎓 You learned %s! Cost: $%d.",
                    skillToLearn.getName(),
                    skillToLearn.getCost());
            return new ActionResult(true, message);
        } else {
            return new ActionResult(false, "Not enough money to learn " + skillToLearn.getName() + "!");
        }
    }

    public ActionResult completeTask(model.Task task) {
        if (task == null) {
            return new ActionResult(false, "Task not found.");
        }
        if (userController.getEnergy() >= task.getEnergyCost()) {
            userController.addBalance(task.getPayment());
            userController.decreaseEnergy(task.getEnergyCost());
            userController.notifyObservers();
            String message = String.format("Completed task '%s'. Earned $%d, used %d energy.",
                    task.getName(), task.getPayment(), task.getEnergyCost());
            return new ActionResult(true, message);
        } else {
            return new ActionResult(false, "Not enough energy to complete this task!");
        }
    }

    public UserController getUserController() {
        return userController;
    }
    
    // Routine actions can be handled similarly, returning results or status objects
    // ...
    
    // Helper class for action results
    public static class ActionResult {
        public final boolean success;
        public final String message;

        public ActionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
