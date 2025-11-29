package model.player;

import java.util.List;

import model.goals.AchievementStats;
import model.goals.Challenge;
import model.goals.ChallengeType;

// The goal surface the rule services need: challenge progress, achievement
// unlocks, and the rewards both of them hand out.
// Implemented by UserController (so every change notifies observers) and by
// test doubles that have no aggregate behind them.
public interface PlayerProgress extends AchievementStats {

    // ----- Challenges -----
    List<Challenge> getActiveChallenges();

    List<Challenge> updateChallengeProgress(ChallengeType type, int amount);

    void addChallenges(List<Challenge> challenges);

    // ----- Achievements -----
    void unlockAchievement(String id);

    // ----- Rewards -----
    boolean deposit(int amount);

    boolean addExperience(int amount);

    void gainReputation(int amount);
}
