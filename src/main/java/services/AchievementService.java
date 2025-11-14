package services;

import model.Achievement;
import model.User;
import java.util.ArrayList;
import java.util.List;
import provider.AchievementProvider;

// Checks player stats and unlocks earned achievements with rewards.
public class AchievementService {
    private static AchievementService instance;

    private AchievementService() {
    }

    public static AchievementService getInstance() {
        if (instance == null) {
            instance = new AchievementService();
        }
        return instance;
    }

    // Checks every achievement, unlocks new ones, and hands out rewards.
    public List<Achievement> checkAndUnlockAchievements(User user) {
        ArrayList<Achievement> newOnes = new ArrayList<Achievement>();
        List<Achievement> all = AchievementProvider.getAllAchievements();
        for (int i = 0; i < all.size(); i++) {
            Achievement a = all.get(i);
            if (!user.hasAchievement(a.getId())) {
                if (a.checkRequirement(user)) {
                    user.unlockAchievement(a.getId());
                    a.unlock();
                    user.deposit(a.getRewardMoney());
                    newOnes.add(a);
                }
            }
        }
        return newOnes;
    }

    private boolean checkAchievementRequirement(User user, Achievement achievement) {
        return achievement.checkRequirement(user);
    }

    public int getAchievementProgress(User user, Achievement achievement) {
        return achievement.getProgress(user);
    }

    // Return all achievements with unlocked status updated
    public List<Achievement> getAchievementsWithStatus(User user) {
        ArrayList<Achievement> result = new ArrayList<Achievement>();
        List<Achievement> all = AchievementProvider.getAllAchievements();
        for (int i = 0; i < all.size(); i++) {
            Achievement a = all.get(i);
            if (user.hasAchievement(a.getId())) {
                a.unlock();
            }
            result.add(a);
        }
        return result;
    }
}
