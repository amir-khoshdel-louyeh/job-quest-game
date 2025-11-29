package provider;

import java.util.List;

import model.goals.Achievement;

// Read-only achievement-template contract.
public interface AchievementCatalog {
    List<Achievement> getAllAchievements();
}
