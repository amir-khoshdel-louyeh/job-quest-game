package services;

import model.player.User;
import model.goals.Achievement;
import java.util.ArrayList;
import java.util.List;

// Checks player stats and unlocks earned achievements with rewards.
// Templates come from outside; injected into GameController.
public class AchievementService {
    private final java.util.function.Supplier<List<Achievement>> allSource;

    // ----- Construction -----
    public AchievementService() {
        provider.AchievementProvider defaults = new provider.AchievementProvider();
        this.allSource = defaults::getAllAchievements;
    }

    // Custom template list for tests.
    public AchievementService(java.util.function.Supplier<List<Achievement>> allSource) {
        if (allSource == null) {
            throw new IllegalArgumentException("achievement source must not be null");
        }
        this.allSource = allSource;
    }

    // ----- Commands -----
    // Unlocks new achievements and hands out rewards.
    public List<Achievement> checkAndUnlockAchievements(User user) {
        ArrayList<Achievement> newOnes = new ArrayList<Achievement>();
        List<Achievement> all = allSource.get();
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

    // ----- Queries -----
    public int getAchievementProgress(User user, Achievement achievement) {
        return achievement.getProgress(user);
    }

    public List<Achievement> getAchievementsWithStatus(User user) {
        return allSource.get();
    }
}
