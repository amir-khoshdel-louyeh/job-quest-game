package model.player;

import java.util.ArrayList;

import model.career.SkillSet;
import utils.Require;

// Tracks learned skills.
// Used by model.player.Player (sole owner); callers only ever see SkillSet,
// so the concrete book cannot be downcast and mutated.
final class SkillBook implements SkillSet {
    private final ArrayList<String> skills = new ArrayList<String>();

    // ----- Queries -----
    public boolean hasSkill(String skillName) {
        return skills.contains(Require.notNull(skillName, "skillName"));
    }

    @Override
    public int getSkillCount() {
        return skills.size();
    }

    // ----- Commands -----
    public boolean addSkill(String skillName) {
        String validName = Require.notNull(skillName, "skillName");
        if (validName.trim().equals("")) {
            throw new IllegalArgumentException("skillName required");
        }
        if (skills.contains(validName)) {
            return false;
        }
        skills.add(validName);
        return true;
    }
}
