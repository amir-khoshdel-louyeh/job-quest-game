package model.career;

import java.util.Collection;

// Query-only view of learned skills.
// Implemented by SkillBook; used by Job checks.
public interface SkillSet {
    boolean hasSkill(String skillName);

    int getSkillCount();

    // True when every required skill is present (empty requirement = true).
    default boolean hasAllSkills(Collection<String> required) {
        if (required == null) {
            return false;
        }
        for (String skill : required) {
            if (!hasSkill(skill)) {
                return false;
            }
        }
        return true;
    }
}
