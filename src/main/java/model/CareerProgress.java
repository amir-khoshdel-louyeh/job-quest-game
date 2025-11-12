package model;

/**
 * OOP Step 6: level/XP/reputation extracted from the {@link User} god-object.
 *
 * <p>Single Responsibility: professional growth math only. Pure — it never
 * touches {@link Vitals}; level-up recovery is orchestrated by {@link User}
 * (which owns both) to avoid a circular dependency.</p>
 */
public class CareerProgress {
    public static final int MAX_LEVEL = 100;

    private int level = 1;
    private int experience = 0;
    private int reputation = 0; // 0-100

    public int getLevel() { return level; }

    public void setLevel(int level) {
        this.level = Math.max(1, Math.min(MAX_LEVEL, level));
    }

    public int getExperience() { return experience; }

    /**
     * Set XP and check for level up.
     * @return true when a level was gained
     */
    public boolean setExperience(int experience) {
        this.experience = Math.max(0, experience);
        return checkLevelUp();
    }

    /**
     * Add XP and check for level up.
     * @return true when a level was gained
     */
    public boolean addExperience(int amount) {
        if (amount <= 0) return false;
        this.experience += amount;
        return checkLevelUp();
    }

    private boolean checkLevelUp() {
        int requiredXP = getRequiredExperienceForNextLevel();
        if (experience >= requiredXP && level < MAX_LEVEL) {
            level++;
            experience -= requiredXP;
            return true;
        }
        return false;
    }

    public int getRequiredExperienceForNextLevel() {
        return level * 1000; // Each level requires level * 1000 XP
    }

    public int getExperiencePercentage() {
        return (int) ((experience / (double) getRequiredExperienceForNextLevel()) * 100);
    }

    public int getReputation() { return reputation; }

    public void setReputation(int reputation) {
        this.reputation = Math.max(0, Math.min(100, reputation));
    }

    public void addReputation(int amount) {
        setReputation(reputation + amount);
    }

    public String getReputationTitle() {
        if (reputation >= 90) return "Legendary";
        if (reputation >= 75) return "Expert";
        if (reputation >= 60) return "Professional";
        if (reputation >= 40) return "Skilled";
        if (reputation >= 20) return "Beginner";
        return "Newbie";
    }
}
