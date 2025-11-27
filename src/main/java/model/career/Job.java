package model.career;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Identity;

// One piece of work the player can do for pay.
// Checked against Identity + SkillSet; read via JobProvider/JobController.
public final class Job {
    private final String name;
    private final String description;
    private final int payment;
    private final int energyCost;
    private final Identity requiredIdentity;
    private final ArrayList<String> requiredSkills;

    // ----- Construction -----
    // Builds a job; null skills become an empty requirement.
    public Job(String name, String description, int payment, int energyCost, Identity requiredIdentity, List<String> requiredSkills) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("name required");
        }
        if (payment < 0 || energyCost < 0) {
            throw new IllegalArgumentException("payment/energyCost must be >= 0");
        }
        if (requiredIdentity == null) {
            throw new IllegalArgumentException("requiredIdentity is null");
        }
        this.name = name;
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
        this.payment = payment;
        this.energyCost = energyCost;
        this.requiredIdentity = requiredIdentity;
        if (requiredSkills == null) {
            this.requiredSkills = new ArrayList<String>();
        } else {
            this.requiredSkills = new ArrayList<String>(requiredSkills);
        }
    }

    // ----- Getter -----
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

    public List<String> getRequiredSkills() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < requiredSkills.size(); i++) {
            copy.add(requiredSkills.get(i));
        }
        return copy;
    }

    // ----- Queries -----
    public boolean isForIdentity(Identity identity) {
        return identity != null && requiredIdentity.isSameProfession(identity);
    }

    // True when the profession matches and every needed skill is present.
    public boolean isAvailableFor(Identity identity, SkillSet skills) {
        if (identity == null || skills == null) {
            return false;
        }
        if (!requiredIdentity.isSameProfession(identity)) {
            return false;
        }
        return skills.hasAllSkills(requiredSkills);
    }

    // Lists required skills still missing (empty = ready to work).
    public List<String> missingSkills(SkillSet skills) {
        ArrayList<String> missing = new ArrayList<String>();
        if (skills == null) {
            missing.addAll(requiredSkills);
            return missing;
        }
        for (int i = 0; i < requiredSkills.size(); i++) {
            String need = requiredSkills.get(i);
            if (!skills.hasSkill(need)) {
                missing.add(need);
            }
        }
        return missing;
    }

    // ----- Object overrides -----
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Job other = (Job) obj;
        return payment == other.payment
                && energyCost == other.energyCost
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description)
                && Objects.equals(requiredIdentity, other.requiredIdentity)
                && Objects.equals(requiredSkills, other.requiredSkills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, payment, energyCost, requiredIdentity, requiredSkills);
    }

    @Override
    public String toString() {
        return name + " [pay $" + payment + ", energy -" + energyCost + "]";
    }
}
