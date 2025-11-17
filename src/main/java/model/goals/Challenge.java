package model.goals;

// A mission the player can finish for extra rewards.
// Base is concrete for compatibility, but each ChallengeType has a
// subclass (like Achievement): new kinds extend, not switch (OCP).
// copy() is polymorphic so services clone without "new Challenge".
public class Challenge {
    private final String id;
    private final String name;
    private final String description;
    private final ChallengeType type;
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
    
    public Challenge(String id, String name, String description, ChallengeType type,
                int targetValue, int rewardMoney, int rewardExperience) {
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
        this.targetValue = targetValue;
        this.rewardMoney = rewardMoney;
        this.rewardExperience = rewardExperience;
        this.currentProgress = 0;
        this.completed = false;
    }

    // Polymorphic clone with zero progress: subclasses return their own type.
    public Challenge copy() {
        Challenge c = new Challenge(id, name, description, type, targetValue, rewardMoney, rewardExperience);
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
        if (!completed) {
            currentProgress = currentProgress + amount;
            
            if (currentProgress >= targetValue) {
                currentProgress = targetValue;
                completed = true;
            }
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
    
    @Override
    public String toString() {
        return name + " [" + currentProgress + "/" + targetValue + "] - Reward: $" + rewardMoney + " + " + rewardExperience + " XP";
    }
}
