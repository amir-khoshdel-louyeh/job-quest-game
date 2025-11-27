package model.goals;

import java.util.Objects;

// An unlockable goal with a reward.
// Checked against AchievementStats; read via AchievementProvider/AchievementService.
public class Achievement {
    private final String id;
    private final String name;
    private final String description;
    private final int rewardMoney;
    private final int requiredValue;
    private final java.util.function.ToIntFunction<AchievementStats> valueExtractor;

    // ----- Construction -----
    // Builds an achievement; valueExtractor reads the current value from stats.
    public Achievement(String id, String name, String description, int rewardMoney,
                       int requiredValue,
                       java.util.function.ToIntFunction<AchievementStats> valueExtractor) {
        if (id == null || id.trim().equals("")) {
            throw new IllegalArgumentException("id required");
        }
        if (requiredValue <= 0) {
            throw new IllegalArgumentException("requiredValue must be > 0");
        }
        if (valueExtractor == null) {
            throw new IllegalArgumentException("valueExtractor required");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.rewardMoney = rewardMoney;
        this.requiredValue = requiredValue;
        this.valueExtractor = valueExtractor;
    }

    // ----- Getter -----
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getRewardMoney() { return rewardMoney; }
    public int getRequiredValue() { return requiredValue; }

    // ----- Queries -----
    public boolean isUnlocked(AchievementStats stats) {
        if (stats == null) {
            return false;
        }
        return stats.hasAchievement(id);
    }

    protected int getCurrentValue(AchievementStats stats) {
        return valueExtractor.applyAsInt(stats);
    }

    // True when the current value reaches the requirement.
    public boolean checkRequirement(AchievementStats stats) {
        if (stats == null) {
            return false;
        }
        return getCurrentValue(stats) >= getRequiredValue();
    }

    // Progress from 0 to 100.
    public int getProgress(AchievementStats stats) {
        if (stats == null) {
            return 0;
        }
        return utils.ProgressUtil.percent(getCurrentValue(stats), getRequiredValue());
    }

    public String toString() {
        return name + " - " + description;
    }

    // Equal by id.
    // ----- Object overrides -----
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Achievement other = (Achievement) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
