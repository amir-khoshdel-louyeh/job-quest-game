package model.player;

import model.career.SkillSet;

// Learned skills, read through the query-only SkillSet.
public interface SkillView {
    SkillSet getSkillSet();

    boolean hasSkill(String skillName);
}
