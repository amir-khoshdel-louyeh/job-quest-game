package model.goals;

// The achievement operations a rule needs. Implemented by User and by
// UserController, so services never depend on the whole aggregate.
public interface AchievementLog {
    boolean hasAchievement(String id);

    void unlockAchievement(String id);
}
