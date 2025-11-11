package model;

/**
 * Represents a skill that can be learned at the school.
 * This is a data object that encapsulates the properties of a learnable skill.
 *
 * Each skill declares which professional identities it belongs to
 * (many-to-many: e.g. "Content Writing" serves Typist and Doctor, while
 * "Advanced SEO" is Programmer-only). The school only offers a character
 * the skills of its own profession, so a Doctor never sees SEO lessons.
 */
public class LearnableSkill {
    private final String name;
    private final String description;
    private final int cost;
    private final long requiredPlayTimeMinutes; // The playtime in minutes required to unlock this skill.
    private final java.util.List<String> identities; // Identity simple names, e.g. "Doctor".

    public LearnableSkill(String name, String description, int cost, long requiredPlayTimeMinutes,
                          java.util.List<String> identities) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.requiredPlayTimeMinutes = requiredPlayTimeMinutes;
        this.identities = identities != null
                ? java.util.Collections.unmodifiableList(new java.util.ArrayList<>(identities))
                : java.util.Collections.emptyList();
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

    /** Identity simple names this skill belongs to (unmodifiable). */
    public java.util.List<String> getIdentities() { return identities; }

    /** Check if this skill is taught to the given identity. */
    public boolean isForIdentity(String identitySimpleName) {
        return identities.contains(identitySimpleName);
    }

}