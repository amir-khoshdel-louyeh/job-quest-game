package services;

import model.player.User;
import model.goals.Achievement;
import java.util.ArrayList;
import java.util.List;
import provider.AchievementProvider;

// Checks player stats and unlocks earned achievements with rewards.
public class AchievementService {
    private AchievementService() {
    }

    // Checks every achievement, unlocks new ones, and hands out rewards.
    public static List<Achievement> checkAndUnlockAchievements(User user) {
        ArrayList<Achievement> newOnes = new ArrayList<Achievement>();
        List<Achievement> all = AchievementProvider.getAllAchievements();
        for (int i = 0; i < all.size(); i++) {
            Achievement a = all.get(i);
            if (!user.hasAchievement(a.getId())) {
                if (a.checkRequirement(user)) {
                    user.unlockAchievement(a.getId());
                    user.deposit(a.getRewardMoney());
                    newOnes.add(a);
                }
            }
        }
        return newOnes;
    }

    public static int getAchievementProgress(User user, Achievement achievement) {
        return achievement.getProgress(user);
    }

    // Achievements are immutable; unlocked status is derived from User.
    public static List<Achievement> getAchievementsWithStatus(User user) {
        return AchievementProvider.getAllAchievements();
    }
}
