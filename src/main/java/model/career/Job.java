package model.career;

import java.util.ArrayList;
import java.util.List;

import model.Identity;

// One piece of work the player can do for pay.
// Depends only on Identity + skill names, never on User:
// keeps model.career free of model.player (no package cycle).
// Immutable value object: all fields final, validated in constructor.
public final class Job {
    private final String name;
    private final String description;
    private final int payment;
    private final int energyCost;
    private final Identity requiredIdentity;
    private final ArrayList<String> requiredSkills;

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

    public Identity getRequiredIdentity() {
        return requiredIdentity;
    }

    // Returns a copy so the stored list can't be changed from outside.
    public List<String> getRequiredSkills() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < requiredSkills.size(); i++) {
            copy.add(requiredSkills.get(i));
        }
        return copy;
    }

    public boolean isForIdentity(Identity identity) {
        if (identity == null) {
            return false;
        }
        return requiredIdentity.isSameProfession(identity);
    }

    // True when the profession matches and the player has every needed skill.
    public boolean isAvailableFor(Identity identity, List<String> userSkills) {        if (identity == null || userSkills == null) {
            return false;
        }
        if (!requiredIdentity.isSameProfession(identity)) {
            return false;
        }
        for (int i = 0; i < requiredSkills.size(); i++) {
            if (!userSkills.contains(requiredSkills.get(i))) {
                return false;
            }
        }
        return true;
    }
}
