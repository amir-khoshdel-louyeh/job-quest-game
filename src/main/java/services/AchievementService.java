package services;

import model.goals.Achievement;
import model.player.PlayerProgress;
import provider.contract.AchievementCatalog;
import java.util.ArrayList;
import java.util.List;

import utils.Require;

// Checks player stats and unlocks earned achievements with rewards.
// The catalog is injected, so this rule layer never constructs its own copy.
public final class AchievementService implements AchievementRules {
    private final AchievementCatalog catalog;

    // ----- Construction -----
    // Every dependency is required; GameSession supplies the shared catalog.
    public AchievementService(AchievementCatalog catalog) {
        this.catalog = Require.notNull(catalog, "achievement catalog");
    }

    // ----- Commands -----
    // Unlocks new achievements and hands out rewards.
    @Override
    public List<Achievement> checkAndUnlockAchievements(PlayerProgress player) {
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
    @Override
    public List<Achievement> getAllAchievements() {
        return catalog.getAllAchievements();
    }
}
