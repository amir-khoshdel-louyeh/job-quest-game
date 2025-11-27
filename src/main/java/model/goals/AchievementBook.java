package model.goals;

import java.util.ArrayList;
import java.util.List;

// Owns unlocked achievements.
// Used by model.player.User (sole owner).
public class AchievementBook {
    private ArrayList<String> unlockedAchievements = new ArrayList<String>();

    // ----- Getter -----
    public List<String> getUnlockedAchievements() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < unlockedAchievements.size(); i++) {
            copy.add(unlockedAchievements.get(i));
        }
        return copy;
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
