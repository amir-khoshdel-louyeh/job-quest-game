package model;

import java.util.ArrayList;
import java.util.List;

// One piece of work the player can do for pay.
public class Job {
    private String name;
    private String description;
    private int payment;
    private int energyCost;
    private String requiredIdentity;
    private ArrayList<String> requiredSkills;

    public Job(String name, String description, int payment, int energyCost, String requiredIdentity, List<String> requiredSkills) {
        this.name = name;
        this.description = description;
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

    public String getRequiredIdentity() {
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

    // Returns the needed skill names as Skill objects.
    public List<Skill> getRequiredSkillsAsObjects() {
        ArrayList<Skill> result = new ArrayList<Skill>();
        for (int i = 0; i < requiredSkills.size(); i++) {
            String skillName = requiredSkills.get(i);
            result.add(new Skill(skillName));
        }
        return result;
    }

    public boolean isForIdentity(String userIdentity) {
        return requiredIdentity.equals(userIdentity);
    }

    public boolean isForIdentity(Identity identity) {
        if (identity == null) {
            return false;
        }
        return requiredIdentity.equals(identity.getProfessionName());
    }

    // True when the profession matches and the player has every needed skill.
    public boolean isAvailableFor(String userIdentity, List<String> userSkills) {
        if (!userIdentity.equals(requiredIdentity)) {
            return false;
        }
        for (int i = 0; i < requiredSkills.size(); i++) {
            String need = requiredSkills.get(i);
            if (!userSkills.contains(need)) {
                return false;
            }
        }
        return true;
    }

    // True when the profession matches and the player has every needed skill.
    public boolean isAvailableFor(Identity identity, List<Skill> userSkills) {
        if (identity == null || userSkills == null) {
            return false;
        }
        if (!requiredIdentity.equals(identity.getProfessionName())) {
            return false;
        }
        List<Skill> needed = getRequiredSkillsAsObjects();
        for (int i = 0; i < needed.size(); i++) {
            if (!userSkills.contains(needed.get(i))) {
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
