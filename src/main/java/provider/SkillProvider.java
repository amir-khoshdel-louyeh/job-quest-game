package provider;

import java.util.ArrayList;
import java.util.List;

import model.LearnableSkill;
import model.User;

// This class gives us all skills that can be learned at school.
public class SkillProvider {

    // list of all skills
    private static ArrayList<LearnableSkill> ALL_SKILLS = new ArrayList<LearnableSkill>();

    // fill the list once when class loads
    static {
        ALL_SKILLS.add(new LearnableSkill("Typing Proficiency", "Improves speed on basic tasks.", 500, 0, makeList2("Typist", "Doctor")));
        ALL_SKILLS.add(new LearnableSkill("Basic Graphic Design", "Unlocks simple design jobs.", 1500, 30, makeList1("LogoDesigner")));
        ALL_SKILLS.add(new LearnableSkill("Content Writing", "Unlocks article writing jobs.", 2000, 60, makeList2("Typist", "Doctor")));
        ALL_SKILLS.add(new LearnableSkill("Web Development Basics", "Unlocks entry-level web tasks.", 5000, 120, makeList2("Programmer", "LogoDesigner")));
        ALL_SKILLS.add(new LearnableSkill("Advanced SEO", "Boosts earnings from web-related jobs.", 10000, 240, makeList1("Programmer")));
        ALL_SKILLS.add(new LearnableSkill("Project Management", "Unlocks high-paying management tasks.", 25000, 480, makeList2("Programmer", "Doctor")));
    }

    // Helper to make list with one name (beginner way, no List.of)
    private static ArrayList<String> makeList1(String s1) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(s1);
        return list;
    }

    // Helper to make list with two names
    private static ArrayList<String> makeList2(String s1, String s2) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(s1);
        list.add(s2);
        return list;
    }

    // Find skill by name, uses simple for loop
    public static LearnableSkill getSkill(String name) {
        for (int i = 0; i < ALL_SKILLS.size(); i++) {
            LearnableSkill skill = ALL_SKILLS.get(i);
            if (skill.getName().equals(name)) {
                return skill;
            }
        }
        return null;
    }

    // Return all skills (copy)
    public static List<LearnableSkill> getAllSkills() {
        ArrayList<LearnableSkill> copy = new ArrayList<LearnableSkill>();
        for (int i = 0; i < ALL_SKILLS.size(); i++) {
            copy.add(ALL_SKILLS.get(i));
        }
        return copy;
    }

    // Return skills for one profession name, uses for loop
    public static List<LearnableSkill> getSkillsForIdentity(String identityName) {
        ArrayList<LearnableSkill> result = new ArrayList<LearnableSkill>();
        for (int i = 0; i < ALL_SKILLS.size(); i++) {
            LearnableSkill skill = ALL_SKILLS.get(i);
            if (skill.isForIdentity(identityName)) {
                result.add(skill);
            }
        }
        return result;
    }

    // Return skills for one Identity object
    public static List<LearnableSkill> getSkillsForIdentity(model.Identity identity) {
        ArrayList<LearnableSkill> result = new ArrayList<LearnableSkill>();
        if (identity == null) {
            return result;
        }
        for (int i = 0; i < ALL_SKILLS.size(); i++) {
            LearnableSkill skill = ALL_SKILLS.get(i);
            if (skill.isForIdentity(identity)) {
                result.add(skill);
            }
        }
        return result;
    }

    // Return skills that user can learn now (own profession, enough playtime, not learned yet)
    public static List<LearnableSkill> getAvailableSkillsForUser(User user) {
        ArrayList<LearnableSkill> result = new ArrayList<LearnableSkill>();
        if (user == null || user.getIdentity() == null) {
            return result;
        }
        long minutesPlayed = user.getTotalPlayTime() / (1000 * 60);
        for (int i = 0; i < ALL_SKILLS.size(); i++) {
            LearnableSkill skill = ALL_SKILLS.get(i);
            if (!skill.isForIdentity(user.getIdentity())) {
                continue;
            }
            if (minutesPlayed >= skill.getRequiredPlayTimeMinutes()) {
                if (!user.hasSkill(skill.getName())) {
                    result.add(skill);
                }
            }
        }
        return result;
    }
}
