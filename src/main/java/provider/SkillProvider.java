package provider;

import java.util.ArrayList;
import java.util.List;

import model.Identity;
import model.career.LearnableSkill;
import model.player.User;

// Holds all skills the player can learn at school.
public class SkillProvider {

    private static ArrayList<LearnableSkill> ALL_SKILLS = new ArrayList<LearnableSkill>();

    static {
        // 10-hour run tuning: time gates cut from max 480min (80% of budget)
        // to max 110min, costs cut ~3x so a full profession path (~5-8k total)
        // is affordable in the first 2-3 hours.
        ALL_SKILLS.add(new LearnableSkill("Typing Proficiency", "Improves speed on basic tasks.", 300, 0, makeList2(IdentityProvider.createIdentity("Typist"), IdentityProvider.createIdentity("Doctor"))));
        ALL_SKILLS.add(new LearnableSkill("Basic Graphic Design", "Unlocks simple design jobs.", 800, 10, makeList1(IdentityProvider.createIdentity("LogoDesigner"))));
        ALL_SKILLS.add(new LearnableSkill("Content Writing", "Unlocks article writing jobs.", 1200, 25, makeList2(IdentityProvider.createIdentity("Typist"), IdentityProvider.createIdentity("Doctor"))));
        ALL_SKILLS.add(new LearnableSkill("Web Development Basics", "Unlocks entry-level web tasks.", 2500, 45, makeList2(IdentityProvider.createIdentity("Programmer"), IdentityProvider.createIdentity("LogoDesigner"))));
        ALL_SKILLS.add(new LearnableSkill("Advanced SEO", "Boosts earnings from web-related jobs.", 4000, 75, makeList1(IdentityProvider.createIdentity("Programmer"))));
        ALL_SKILLS.add(new LearnableSkill("Project Management", "Unlocks high-paying management tasks.", 8000, 110, makeList2(IdentityProvider.createIdentity("Programmer"), IdentityProvider.createIdentity("Doctor"))));
    }

    private static ArrayList<Identity> makeList1(Identity i1) {
        ArrayList<Identity> list = new ArrayList<Identity>();
        list.add(i1);
        return list;
    }

    private static ArrayList<Identity> makeList2(Identity i1, Identity i2) {
        ArrayList<Identity> list = new ArrayList<Identity>();
        list.add(i1);
        list.add(i2);
        return list;
    }

    public static LearnableSkill getSkill(String name) {
        for (int i = 0; i < ALL_SKILLS.size(); i++) {
            LearnableSkill skill = ALL_SKILLS.get(i);
            if (skill.getName().equals(name)) {
                return skill;
            }
        }
        return null;
    }

    public static List<LearnableSkill> getAllSkills() {
        ArrayList<LearnableSkill> copy = new ArrayList<LearnableSkill>();
        for (int i = 0; i < ALL_SKILLS.size(); i++) {
            copy.add(ALL_SKILLS.get(i));
        }
        return copy;
    }

    public static List<LearnableSkill> getSkillsForIdentity(Identity identity) {
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
