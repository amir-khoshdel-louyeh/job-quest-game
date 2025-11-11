package model;

import java.util.Collections;
import java.util.List;

/**
 * Represents a single job or task a user can perform.
 */
public class Job {
    private final String name;
    private final String description;
    private final int payment;
    private final int energyCost;
    private final String requiredIdentity; // Simple class name, e.g., "Programmer"
    private final List<String> requiredSkills; // List of skill names

    public Job(String name, String description, int payment, int energyCost, String requiredIdentity, List<String> requiredSkills) {
        this.name = name;
        this.description = description;
        this.payment = payment;
        this.energyCost = energyCost;
        this.requiredIdentity = requiredIdentity;
        this.requiredSkills = requiredSkills != null ? requiredSkills : Collections.emptyList();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPayment() {
        return payment;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public String getRequiredIdentity() {
        return requiredIdentity;
    }

    public List<String> getRequiredSkills() {
        return Collections.unmodifiableList(requiredSkills);
    }

    /** Required skills as Skill objects (object-based API, no String mapping needed). */
    public List<Skill> getRequiredSkillsAsObjects() {
        return requiredSkills.stream().map(Skill::new).toList();
    }

    /** Check if this job belongs to the given identity's profession. */
    public boolean isForIdentity(String userIdentity) {
        return requiredIdentity.equals(userIdentity);
    }

    /**
     * OOP Step 1: object-based overload — callers pass the Identity object,
     * never {@code getClass().getSimpleName()}. Null-safe.
     */
    public boolean isForIdentity(Identity identity) {
        if (identity == null) return false;
        return requiredIdentity.equals(identity.getProfessionName());
    }

    /** Check if the user is eligible to take this job */
    public boolean isAvailableFor(String userIdentity, List<String> userSkills) {
        return userIdentity.equals(requiredIdentity) && userSkills.containsAll(requiredSkills);
    }

    /**
     * OOP Step 1: object-based eligibility. Compares Skill objects via
     * {@link Skill#equals(Object)} instead of raw Strings.
     */
    public boolean isAvailableFor(Identity identity, List<Skill> userSkills) {
        if (identity == null || userSkills == null) return false;
        return requiredIdentity.equals(identity.getProfessionName())
                && userSkills.containsAll(getRequiredSkillsAsObjects());
    }

    /** Convenience: check eligibility directly from a User (Tell, Don't Ask). */
    public boolean isAvailableFor(User user) {
        if (user == null || user.getIdentity() == null) return false;
        return isAvailableFor(user.getIdentity(), user.getSkills());
    }
}