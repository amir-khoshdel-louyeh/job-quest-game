package model.player;

import java.util.ArrayList;

import model.career.SkillSet;

// Tracks learned skills.
// Used by model.player.Player (sole owner); callers only ever see SkillSet,
// so the concrete book cannot be downcast and mutated.
final class SkillBook implements SkillSet {
    private final ArrayList<String> skills = new ArrayList<String>();

    // ----- Queries -----
    public boolean hasSkill(String skillName) {
        if (skillName == null) {
            return false;
        }
        return skills.contains(skillName);
    }

    @Override
    public int getSkillCount() {
        return skills.size();
    }

    // ----- Commands -----
    public boolean addSkill(String skillName) {
        if (skillName == null || skillName.trim().equals("")) {
            return false;
        }
        if (skills.contains(skillName)) {
            return false;
        }
        skills.add(skillName);
        return true;
    }
}
