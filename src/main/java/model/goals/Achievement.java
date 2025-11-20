package model.goals;

import java.util.Objects;

// An unlockable goal with a reward.
// Depends only on AchievementStats (DIP), never on player.User:
// no goals -> player package cycle.
// One concrete class + Strategy (value extractor): the old per-metric
// subclasses differed by a single line, so inheritance was class
// explosion — composition carries the varying part instead.
// New metrics pass a lambda; this class never changes (OCP).
public class Achievement {
    private final String id;
    private final String name;
    private final String description;
    private final int rewardMoney;
    private final int requiredValue;
    private final java.util.function.ToIntFunction<AchievementStats> valueExtractor;

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

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getRewardMoney() { return rewardMoney; }
    public int getRequiredValue() { return requiredValue; }

    public boolean isUnlocked(AchievementStats stats) {
        if (stats == null) {
            return false;
        }
        return stats.hasAchievement(id);
    }

    protected int getCurrentValue(AchievementStats stats) {
        return valueExtractor.applyAsInt(stats);
    }

    public boolean checkRequirement(AchievementStats stats) {
        if (stats == null) {
            return false;
        }
        return getCurrentValue(stats) >= getRequiredValue();
    }

    public int getProgress(AchievementStats stats) {
        if (stats == null) {
            return 0;
        }
        return utils.ProgressUtil.percent(getCurrentValue(stats), getRequiredValue());
    }

    public String toString() {
        return name + " - " + description;
    }

    // Entity equality by id: unlock state lives in AchievementBook
    // (per player), not in this shared definition object.
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
