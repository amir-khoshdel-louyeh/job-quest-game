package model;

/**
 * Represents a challenge/mission that players can complete for rewards.
 */
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
        COMPLETE_JOBS,      // Complete X jobs
        EARN_MONEY,         // Earn X money
        LEARN_SKILLS,       // Learn X skills
        MAINTAIN_HEALTH,    // Keep health above X for Y days
        SPEND_ENERGY,       // Spend X energy on work
        BUY_ITEMS          // Buy X items from shop
    }
    
    public Challenge(String id, String name, String description, ChallengeType type,
                int targetValue, int rewardMoney, int rewardExperience) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.targetValue = targetValue;
        this.rewardMoney = rewardMoney;
        this.rewardExperience = rewardExperience;
        this.currentProgress = 0;
        this.completed = false;
    }
    
    public void addProgress(int amount) {
        if (!completed) {
            currentProgress = Math.min(currentProgress + amount, targetValue);
            if (currentProgress >= targetValue) {
                completed = true;
            }
        }
    }
    
    public int getProgressPercentage() {
        return (int) ((currentProgress / (double) targetValue) * 100);
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public ChallengeType getType() { return type; }
    public int getTargetValue() { return targetValue; }
    public int getRewardMoney() { return rewardMoney; }
    public int getRewardExperience() { return rewardExperience; }
    public int getCurrentProgress() { return currentProgress; }
    public boolean isCompleted() { return completed; }
    
    public void setCurrentProgress(int progress) { this.currentProgress = progress; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    
    public String toString() {
        return String.format("%s [%d/%d] - Reward: $%d + %d XP",
            name, currentProgress, targetValue, rewardMoney, rewardExperience);
    }
}
