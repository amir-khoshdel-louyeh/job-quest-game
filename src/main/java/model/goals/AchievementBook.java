package model.goals;

import java.util.ArrayList;
import java.util.List;

import utils.Lists;

// Owns unlocked achievements.
// Used by model.player.User (sole owner); the id list only leaves as a copy.
public final class AchievementBook implements AchievementLog {
    private final ArrayList<String> unlockedAchievements = new ArrayList<String>();

    // ----- Getter -----
    public List<String> getUnlockedAchievements() {
        return Lists.copyOf(unlockedAchievements);
    }

    public int getUnlockedCount() {
        return unlockedAchievements.size();
    }

    // ----- Commands -----
    public void unlockAchievement(String id) {
        if (id != null && !unlockedAchievements.contains(id)) {
            unlockedAchievements.add(id);
        }
    }

    // ----- Queries -----
    public boolean hasAchievement(String id) {
        return id != null && unlockedAchievements.contains(id);
    }
}
