package model.goals;

import java.util.ArrayList;

import utils.Require;

// Owns unlocked achievements.
// Used by model.player.Player (sole owner); callers only ask for a count or
// whether an id was unlocked, so the stored list never leaves the book.
public final class AchievementBook {
    private final ArrayList<String> unlockedAchievements = new ArrayList<String>();

    // ----- Getter -----
    public int getUnlockedCount() {
        return unlockedAchievements.size();
    }

    // ----- Commands -----
    public void unlockAchievement(String id) {
        String validId = Require.notNull(id, "id");
        if (!unlockedAchievements.contains(validId)) {
            unlockedAchievements.add(validId);
        }
    }

    // ----- Queries -----
    public boolean hasAchievement(String id) {
        return unlockedAchievements.contains(Require.notNull(id, "id"));
    }
}
