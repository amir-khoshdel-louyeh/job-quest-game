package model.career;

import java.util.ArrayList;
import java.util.List;

import model.Identity;
import model.player.User;

// One piece of work the player can do for pay.
public class Job {
    private String name;
    private String description;
    private int payment;
    private int energyCost;
    private Identity requiredIdentity;
    private ArrayList<String> requiredSkills;

    public Job(String name, String description, int payment, int energyCost, Identity requiredIdentity, List<String> requiredSkills) {
        this.name = name;
        this.description = description;
        this.payment = payment;
        this.energyCost = energyCost;
        if (requiredIdentity == null) {
            throw new IllegalArgumentException("requiredIdentity is null");
        }
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

    public boolean isAvailableFor(User user) {
        if (user == null || user.getIdentity() == null) {
            return false;
        }
        return isAvailableFor(user.getIdentity(), user.getSkills());
    }
}
