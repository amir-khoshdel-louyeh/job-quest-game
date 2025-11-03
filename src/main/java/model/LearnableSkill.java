package model;

/**
 * Represents a skill that can be learned at the school.
 * This is a data object that encapsulates the properties of a learnable skill.
 */
public class LearnableSkill {
    private final String name;
    private final String description;
    private final int cost;
    private final long requiredPlayTimeMinutes; // The playtime in minutes required to unlock this skill.

    public LearnableSkill(String name, String description, int cost, long requiredPlayTimeMinutes) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.requiredPlayTimeMinutes = requiredPlayTimeMinutes;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public long getRequiredPlayTimeMinutes() { return requiredPlayTimeMinutes; }

}