package model.career;

import java.util.Collection;

// Query-only view of learned skills. model.career depends on this
// interface (own package), never on model.player — so no package cycle,
// and callers pass behavior (Tell-Don't-Ask) instead of raw List<String>
// with manual contains loops (no primitive obsession / feature envy).
public interface SkillSet {
    boolean hasSkill(String skillName);

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
