package model;

/**
 * Represents an achievement that players can unlock.
 */
public abstract class Achievement {
    private final String id;
    private final String name;
    private final String description;
    private final int rewardMoney;
    private final AchievementType type;
    private final int requiredValue;
    private boolean unlocked;

    public enum AchievementType {
        MONEY_EARNED,      // Total money earned
        JOBS_COMPLETED,    // Total jobs completed
        SKILLS_LEARNED,    // Total skills learned
        DAYS_PLAYED,       // Total days played
        HEALTH_MAINTAINED, // Maintain full health for X days
        ENERGY_EFFICIENT,  // Complete jobs with minimal energy
        RICH_PLAYER,       // Have X money at once
        SKILL_MASTER,      // Learn all skills
        WORKAHOLIC         // Complete X jobs in one session
    }

    public Achievement(String id, String name, String description, int rewardMoney, 
                      AchievementType type, int requiredValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rewardMoney = rewardMoney;
        this.type = type;
        this.requiredValue = requiredValue;
        this.unlocked = false;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getRewardMoney() { return rewardMoney; }
    public AchievementType getType() { return type; }
    public int getRequiredValue() { return requiredValue; }
    public boolean isUnlocked() { return unlocked; }

    public void unlock() { this.unlocked = true; }

    public abstract boolean checkRequirement(model.User user);
    public abstract int getProgress(model.User user);

    
    public String toString() {
        return (unlocked ? "✅ " : "🔒 ") + name + " - " + description;
    }
}
