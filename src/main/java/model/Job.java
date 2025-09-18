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

    /**
     * Checks if a user is eligible to take this job.
     * @param userIdentity The simple name of the user's identity class.
     * @param userSkills A list of names of the skills the user has learned.
     * @return true if the user has the required identity and skills.
     */
    public boolean isAvailableFor(String userIdentity, List<String> userSkills) {
        // Check if the identity matches and if the user's skills contain all required skills for this job.
        return userIdentity.equals(requiredIdentity) && userSkills.containsAll(requiredSkills);
    }
}