package model.career;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Identity;

// A skill offered at school with a cost and requirements.
// Checked against Identity; read via SkillProvider/SkillController.
public final class LearnableSkill {
    private final String name;
    private final String description;
    private final int cost;
    private final long requiredPlayTimeMinutes;
    private final ArrayList<Identity> identities;

    // ----- Construction -----
    public LearnableSkill(String name, String description, int cost, long requiredPlayTimeMinutes,
                          List<Identity> identities) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("name required");
        }
        if (cost < 0 || requiredPlayTimeMinutes < 0) {
            throw new IllegalArgumentException("cost/playTime must be >= 0");
        }
        this.name = name;
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
        this.cost = cost;
        this.requiredPlayTimeMinutes = requiredPlayTimeMinutes;
        this.identities = new ArrayList<Identity>();
        if (identities != null) {
            for (int i = 0; i < identities.size(); i++) {
                this.identities.add(identities.get(i));
            }
        }
    }

    // ----- Getter -----
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public long getRequiredPlayTimeMinutes() {
        return requiredPlayTimeMinutes;
    }

    // ----- Queries -----
    public boolean isForIdentity(Identity identity) {
        if (identity == null) {
            return false;
        }
        for (int i = 0; i < identities.size(); i++) {
            if (identities.get(i).isSameProfession(identity)) {
                return true;
            }
        }
        return false;
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
        LearnableSkill other = (LearnableSkill) obj;
        return cost == other.cost
                && requiredPlayTimeMinutes == other.requiredPlayTimeMinutes
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description)
                && Objects.equals(identities, other.identities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, cost, requiredPlayTimeMinutes, identities);
    }

    @Override
    public String toString() {
        return name + " [cost $" + cost + "]";
    }

}
