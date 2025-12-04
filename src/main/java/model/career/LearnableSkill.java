package model.career;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// A skill offered at school with a cost and requirements.
// Checked against Profession; read via SkillProvider/SkillController.
public final class LearnableSkill {
    private final String name;
    private final String description;
    private final int cost;
    private final long requiredPlayTimeMinutes;
    private final ArrayList<Profession> professions;

    // ----- Construction -----
    public LearnableSkill(String name, String description, int cost, long requiredPlayTimeMinutes,
                          List<Profession> professions) {
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
        this.professions = new ArrayList<Profession>();
        if (professions != null) {
            for (int i = 0; i < professions.size(); i++) {
                this.professions.add(professions.get(i));
            }
        }
    }

    // ----- Queries -----
    // One rule for the school screen and the controller.
    public SkillAvailability availabilityFor(SkillSet learnedSkills, long playTimeMinutes) {
        if (learnedSkills != null && learnedSkills.hasSkill(name)) {
            return SkillAvailability.LEARNED;
        }
        if (playTimeMinutes >= requiredPlayTimeMinutes) {
            return SkillAvailability.AVAILABLE;
        }
        return SkillAvailability.LOCKED;
    }

    // Minutes still needed before this skill unlocks (0 when ready).
    public long minutesUntilUnlocked(long playTimeMinutes) {
        long remaining = requiredPlayTimeMinutes - playTimeMinutes;
        if (remaining < 0) {
            return 0;
        }
        return remaining;
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
    public boolean isForProfession(Profession profession) {
        if (profession == null) {
            return false;
        }
        for (int i = 0; i < professions.size(); i++) {
            if (professions.get(i).isSameProfession(profession)) {
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
                && Objects.equals(professions, other.professions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, cost, requiredPlayTimeMinutes, professions);
    }

    @Override
    public String toString() {
        return name + " [cost $" + cost + "]";
    }

}
