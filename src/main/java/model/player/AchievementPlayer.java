package model.player;

import model.goals.AchievementLog;
import model.goals.AchievementStats;

// What achievement rules need: stats to check, the unlock log, and rewards.
public interface AchievementPlayer extends AchievementStats, AchievementLog, RewardTarget {
}
