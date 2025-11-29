package services;

import java.util.List;

import model.goals.Achievement;
import model.player.PlayerProgress;

// The achievement operations controllers and queries need.
// Callers depend on this contract, not on AchievementService.
public interface AchievementRules {

    List<Achievement> checkAndUnlockAchievements(PlayerProgress player);

    List<Achievement> getAllAchievements();
}
