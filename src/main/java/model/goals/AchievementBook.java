package model.goals;

import java.util.ArrayList;
import java.util.List;

// Owns unlocked achievements. Extracted from User for SRP.
public class AchievementBook {
    private ArrayList<String> unlockedAchievements = new ArrayList<String>();

    public List<String> getUnlockedAchievements() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < unlockedAchievements.size(); i++) {
            copy.add(unlockedAchievements.get(i));
        }
        return copy;
    }

    public void setUnlockedAchievements(List<String> list) {
        unlockedAchievements = new ArrayList<String>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                unlockedAchievements.add(list.get(i));
            }
        }
    }

    public void unlockAchievement(String id) {
        if (id != null && !unlockedAchievements.contains(id)) {
            unlockedAchievements.add(id);
        }
    }

    public boolean hasAchievement(String id) {
        return id != null && unlockedAchievements.contains(id);
    }
}
