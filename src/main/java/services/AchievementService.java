package services;

import model.player.AchievementPlayer;
import model.goals.Achievement;
import provider.AchievementCatalog;
import java.util.ArrayList;
import java.util.List;

// Checks player stats and unlocks earned achievements with rewards.
// The catalog is injected, so this rule layer never constructs its own copy.
public final class AchievementService {
    private final AchievementCatalog catalog;

    // ----- Construction -----
    // Every dependency is required; GameSession supplies the shared catalog.
    public AchievementService(AchievementCatalog catalog) {
        this.catalog = utils.Require.notNull(catalog, "achievement catalog");
    }

    // ----- Commands -----
    // Unlocks new achievements and hands out rewards.
    public List<Achievement> checkAndUnlockAchievements(AchievementPlayer player) {
        ArrayList<Achievement> newOnes = new ArrayList<Achievement>();
        List<Achievement> all = catalog.getAllAchievements();
        for (Achievement achievement : all) {
            if (!achievement.isUnlocked(player) && achievement.checkRequirement(player)) {
                player.unlockAchievement(achievement.getId());
                player.deposit(achievement.getRewardMoney());
                newOnes.add(achievement);
            }
        }
        return newOnes;
    }

    // ----- Queries -----
    public List<Achievement> getAllAchievements() {
        return catalog.getAllAchievements();
    }
}
