package model.goals;

import java.util.Objects;

// A mission the player can finish for extra rewards.
// One concrete class keyed by ChallengeType (former per-type subclasses
// were pure type tags with no behavior, so they were collapsed).
// copy() clones with zero progress; copySnapshot() preserves it.
public class Challenge {
    private final String id;
    private final String name;
    private final String description;
    private final ChallengeType type;
    private final boolean daily;
    private final int targetValue;
    private final int rewardMoney;
    private final int rewardExperience;
    private int currentProgress;
    private boolean completed;
    
    public enum ChallengeType {
        COMPLETE_JOBS,
        EARN_MONEY,
        LEARN_SKILLS
    }
    
    // Primary constructor: daily-ness is explicit data, not an id-prefix
    // convention — no magic-string type check at prune time (OCP).
    public Challenge(String id, String name, String description, ChallengeType type,
                int targetValue, int rewardMoney, int rewardExperience, boolean daily) {
        if (id == null || id.trim().equals("")) {
            throw new IllegalArgumentException("id required");
        }
        if (type == null) {
            throw new IllegalArgumentException("type required");
        }
        if (targetValue <= 0) {
            throw new IllegalArgumentException("targetValue must be > 0");
        }
        if (rewardMoney < 0 || rewardExperience < 0) {
            throw new IllegalArgumentException("rewards must be >= 0");
        }
        this.id = id;
        if (name == null) {
            this.name = "";
        } else {
            this.name = name;
        }
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
        this.type = type;
        this.daily = daily;
        this.targetValue = targetValue;
        this.rewardMoney = rewardMoney;
        this.rewardExperience = rewardExperience;
        this.currentProgress = 0;
        this.completed = false;
    }

    // Daily challenges reset; main-story ones persist once completed.
    public boolean isDaily() {
        return daily;
    }

    // Fresh clone with zero progress, preserving the daily flag.
    public Challenge copy() {
        Challenge c = new Challenge(id, name, description, type, targetValue, rewardMoney, rewardExperience, daily);
        return c;
    }

    // Snapshot clone preserving progress: copy() is polymorphic so the
    // runtime type is kept, then progress is replayed via addProgress
    // (which re-applies the target cap + completed flag identically).
    public Challenge copySnapshot() {
        Challenge c = copy();
        c.addProgress(this.currentProgress);
        return c;
    }
    // Adds to progress but never past the target, and marks finished at the target.
    public void addProgress(int amount) {
        if (amount <= 0 || completed) {
            return;
        }
        currentProgress = currentProgress + amount;
        if (currentProgress >= targetValue) {
            currentProgress = targetValue;
            completed = true;
        }
    }
    // Progress shown as a number from 0 to 100.
    public int getProgressPercentage() {
        return utils.ProgressUtil.percent(currentProgress, targetValue);
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ChallengeType getType() { return type; }
    public int getTargetValue() { return targetValue; }
    public int getRewardMoney() { return rewardMoney; }
    public int getRewardExperience() { return rewardExperience; }
    public int getCurrentProgress() { return currentProgress; }
    
    public boolean isCompleted() { return completed; }

    // Entity equality by id: two snapshots of the same challenge stay
    // equal across progress changes (progress is state, not identity).
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Challenge other = (Challenge) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " [" + currentProgress + "/" + targetValue + "] - Reward: $" + rewardMoney + " + " + rewardExperience + " XP";
    }
}
