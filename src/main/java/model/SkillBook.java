package model;

import java.util.ArrayList;
import java.util.List;

// Owns the learned-skill list. Extracted from User for SRP:
// skill rules change here, not in User.
public class SkillBook {
    private ArrayList<String> skills = new ArrayList<String>();

    public List<String> getSkills() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < skills.size(); i++) {
            copy.add(skills.get(i));
        }
        return copy;
    }

    public void setSkills(List<String> newSkills) {
        skills = new ArrayList<String>();
        if (newSkills != null) {
            for (int i = 0; i < newSkills.size(); i++) {
                skills.add(newSkills.get(i));
            }
        }
    }

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

    public boolean hasSkill(String skillName) {
        if (skillName == null) {
            return false;
        }
        return skills.contains(skillName);
    }
}
