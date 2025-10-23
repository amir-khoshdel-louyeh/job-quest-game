package controller;

import database.DatabaseUtil;
import model.Achievement;
import model.GameEvent;
import model.Item;
import model.Job;
import model.LearnableSkill;
import model.Challenge;
import model.Service;
import model.User;
import model.Skill;
import model.WorkResult;
import provider.ServiceProvider;
import provider.SkillProvider;
import services.*;

import java.util.List;

public class GameController {
    private UserController userController;
    private final long sessionStartTime;
    private int sessionJobsCompleted = 0;
    private final AchievementService achievementService;
    private final ChallengeService challengeService;
    private final EventService eventService;

    public GameController(User user, long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
        this.userController = new UserController(user);
        this.achievementService = AchievementService.getInstance();
        this.challengeService = ChallengeService.getInstance();
        this.eventService = EventService.getInstance();
        
        // Initialize challenges for new users
        challengeService.initializeChallenges(user);
        
        // Check for daily login streak
        updateLoginStreak(user);
    }
    
    /**
     * Update the user's login streak.
     */
    private void updateLoginStreak(User user) {
        long currentTime = System.currentTimeMillis();
        long lastLogin = user.getLastLoginDate();
        
        // Check if this is a consecutive day (within 48 hours)
        long hoursSinceLastLogin = (currentTime - lastLogin) / (1000 * 60 * 60);
        
        if (hoursSinceLastLogin < 48) {
            // Consecutive login
            user.setCurrentStreak(user.getCurrentStreak() + 1);
        } else if (hoursSinceLastLogin >= 48) {
            // Streak broken
            user.setCurrentStreak(1);
        }
        
        user.setLastLoginDate(currentTime);
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
            int payment = job.getPayment();
            
            // Apply reputation bonus (up to +50% at max reputation)
            User user = userController.getUser();
            double reputationBonus = user.getReputation() / 200.0; // 0% to 50%
            payment += (int)(payment * reputationBonus);
            
            // Apply level bonus (+5% per level)
            double levelBonus = (user.getLevel() - 1) * 0.05;
            payment += (int)(payment * levelBonus);
            
            // Apply streak bonus (+1% per streak day, max 30%)
            double streakBonus = Math.min(user.getCurrentStreak() * 0.01, 0.30);
            payment += (int)(payment * streakBonus);
            
            userController.addBalance(payment);
            userController.decreaseEnergy(job.getEnergyCost());
            userController.getUser().addToTotalMoneyEarned(payment);
            userController.getUser().incrementJobsCompleted();
            sessionJobsCompleted++;
            
            // Give experience based on job difficulty
            int xpGain = job.getEnergyCost() / 10;
            userController.getUser().addExperience(xpGain);
            
            // Small chance to gain reputation
            if (Math.random() < 0.3) {
                userController.getUser().addReputation(1);
            }
            
            // Update challenge progress
            List<Challenge> completedChallenges = challengeService.updateChallengeProgress(
                user, Challenge.ChallengeType.COMPLETE_JOBS, 1
            );
            challengeService.updateChallengeProgress(user, Challenge.ChallengeType.EARN_MONEY, payment);
            
            // Check for achievements
            List<Achievement> newAchievements = achievementService.checkAndUnlockAchievements(user);
            
            // Check for random events
            GameEvent event = eventService.checkForRandomEvent(user);
            
            userController.notifyObservers();
            
            StringBuilder message = new StringBuilder();
            message.append(String.format("✅ Completed '%s'\n", job.getName()));
            message.append(String.format("💰 Earned $%d (Base: $%d)\n", payment, job.getPayment()));
            message.append(String.format("⚡ Used %d energy | ⭐ +%d XP\n", 
                job.getEnergyCost(), xpGain));
            
            // Add bonus info
            if (reputationBonus > 0 || levelBonus > 0 || streakBonus > 0) {
                message.append("\n🎁 Bonuses Applied:\n");
                if (reputationBonus > 0) {
                    message.append(String.format("  • Reputation: +%d%%\n", (int)(reputationBonus * 100)));
                }
                if (levelBonus > 0) {
                    message.append(String.format("  • Level: +%d%%\n", (int)(levelBonus * 100)));
                }
                if (streakBonus > 0) {
                    message.append(String.format("  • Streak: +%d%%\n", (int)(streakBonus * 100)));
                }
            }
            
            // Show completed challenges
            if (!completedChallenges.isEmpty()) {
                message.append("\n🏆 Challenge Completed!\n");
                for (Challenge challenge : completedChallenges) {
                    message.append(String.format("  '%s' - $%d + %d XP\n", 
                        challenge.getName(), challenge.getRewardMoney(), challenge.getRewardExperience()));
                }
            }
            
            // Show new achievements
            if (!newAchievements.isEmpty()) {
                message.append("\n🎖️ Achievement Unlocked!\n");
                for (Achievement ach : newAchievements) {
                    message.append(String.format("  '%s' - $%d reward!\n", 
                        ach.getName(), ach.getRewardMoney()));
                }
            }
            
            // Show random event
            if (event != null) {
                message.append(String.format("\n%s %s\n", event.getIcon(), event.getTitle()));
                message.append(String.format("  %s\n", event.getDescription()));
                if (!event.getFormattedEffects().isEmpty()) {
                    message.append(String.format("  Effects: %s\n", event.getFormattedEffects()));
                }
            }
            
            return new ActionResult(true, message.toString());
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
            
            // Update challenge progress
            User user = userController.getUser();
            List<Challenge> completedChallenges = challengeService.updateChallengeProgress(
                user, Challenge.ChallengeType.LEARN_SKILLS, 1
            );
            
            // Check for achievements
            List<Achievement> newAchievements = achievementService.checkAndUnlockAchievements(user);
            
            // Gain reputation for learning
            user.addReputation(2);
            
            userController.notifyObservers(); // Update UI

            StringBuilder message = new StringBuilder();
            message.append(String.format("🎓 You learned %s! Cost: $%d.\n",
                    skillToLearn.getName(), skillToLearn.getCost()));
            
            // Show challenge completions
            if (!completedChallenges.isEmpty()) {
                message.append("\n🏆 Challenge Completed!\n");
                for (Challenge challenge : completedChallenges) {
                    message.append(String.format("  '%s' - $%d + %d XP\n", 
                        challenge.getName(), challenge.getRewardMoney(), challenge.getRewardExperience()));
                }
            }
            
            // Show achievements
            if (!newAchievements.isEmpty()) {
                message.append("\n🎖️ Achievement Unlocked!\n");
                for (Achievement ach : newAchievements) {
                    message.append(String.format("  '%s' - $%d reward!\n", 
                        ach.getName(), ach.getRewardMoney()));
                }
            }
            
            return new ActionResult(true, message.toString());
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
    
    // ============== New Rich Features ==============
    
    /**
     * Get user's current stats including level, XP, reputation, etc.
     */
    public String getDetailedStats() {
        User user = userController.getUser();
        StringBuilder stats = new StringBuilder();
        
        stats.append("═══════════════════════════════════\n");
        stats.append(String.format(" 👤 %s - Level %d\n", user.getUsername(), user.getLevel()));
        stats.append("═══════════════════════════════════\n");
        stats.append(String.format("⭐ XP: %d/%d (%d%%)\n", 
            user.getExperience(), 
            user.getRequiredExperienceForNextLevel(),
            user.getExperiencePercentage()));
        stats.append(String.format("🏆 Reputation: %d/100 (%s)\n", 
            user.getReputation(), 
            user.getReputationTitle()));
        stats.append(String.format("🔥 Streak: %d days (Best: %d)\n", 
            user.getCurrentStreak(), 
            user.getMaxStreak()));
        stats.append(String.format("💰 Total Earned: $%,d\n", user.getTotalMoneyEarned()));
        stats.append(String.format("📊 Jobs Completed: %,d\n", user.getTotalJobsCompleted()));
        stats.append(String.format("🎖️ Achievements: %d unlocked\n", 
            user.getUnlockedAchievements().size()));
        stats.append("═══════════════════════════════════\n");
        
        return stats.toString();
    }
    
    /**
     * Get active challenges.
     */
    public List<Challenge> getActiveChallenges() {
        return challengeService.getActiveIncompleteChallenges(userController.getUser());
    }
    
    /**
     * Get achievements with status.
     */
    public List<Achievement> getAchievements() {
        return achievementService.getAchievementsWithStatus(userController.getUser());
    }
    
    /**
     * Trigger a random event manually (for testing).
     */
    public GameEvent triggerRandomEvent() {
        return eventService.checkForRandomEvent(userController.getUser());
    }
    
    /**
     * Get session statistics.
     */
    public String getSessionStats() {
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        long minutes = sessionDuration / (1000 * 60);
        
        return String.format("Session: %d minutes | Jobs: %d", 
            minutes, sessionJobsCompleted);
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
