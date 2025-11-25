package model.player;

import java.util.ArrayList;
import java.util.List;

// Tracks learned skills.
// Used by model.player.User (sole owner); read via User/UserController.

public class SkillBook implements model.career.SkillSet {
    private ArrayList<String> skills = new ArrayList<String>();

    // ----- Getter -----
    public List<String> getSkills() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < skills.size(); i++) {
            copy.add(skills.get(i));
        }
        return copy;
    }

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
