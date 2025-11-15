package model;

// A mission the player can finish for extra rewards.
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
        LEARN_SKILLS,
        MAINTAIN_HEALTH,
        SPEND_ENERGY
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
        double currentAsDouble = (double) currentProgress;
        double ratio = currentAsDouble / targetValue; 
        double percentage = ratio * 100;
        return (int) percentage;
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
    public void setCurrentProgress(int progress) { this.currentProgress = progress; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    @Override
    public String toString() {
        return name + " [" + currentProgress + "/" + targetValue + "] - Reward: $" + rewardMoney + " + " + rewardExperience + " XP";
    }
}
