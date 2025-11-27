package model.goals;

import java.util.Objects;

// A mission the player can finish for extra rewards.
// Tracked by ChallengeBook; read via ChallengeProvider/ChallengeService.
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
    
    // ----- Construction -----
    // Primary constructor: all challenges are one-shot story goals.
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

    // ----- Commands -----
    // Fresh clone with zero progress.
    public Challenge copy() {
        Challenge c = new Challenge(id, name, description, type, targetValue, rewardMoney, rewardExperience);
        return c;
    }

    // Clone carrying the current progress.
    public Challenge copySnapshot() {
        Challenge c = copy();
        c.addProgress(this.currentProgress);
        return c;
    }
    // Adds progress, capped at the target.
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
    // ----- Queries -----
    // Progress from 0 to 100.
    public int getProgressPercentage() {
        return utils.ProgressUtil.percent(currentProgress, targetValue);
    }
    
    // ----- Getter -----
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ChallengeType getType() { return type; }
    public int getTargetValue() { return targetValue; }
    public int getRewardMoney() { return rewardMoney; }
    public int getRewardExperience() { return rewardExperience; }
    public int getCurrentProgress() { return currentProgress; }
    
    public boolean isCompleted() { return completed; }

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
