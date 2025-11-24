package provider;

import java.util.ArrayList;
import java.util.List;

import model.Identity;
import model.career.LearnableSkill;

// Holds all skills the player can learn at school.
// Single-list catalog: extends AbstractCatalog<LearnableSkill> (lookup
// mechanics inherited). The catalog comes from outside, so callers depend
// on an object they can inject/mock (DIP).
public class SkillProvider extends AbstractCatalog<LearnableSkill> {

    public SkillProvider() {
        this(buildDefaultCatalog());
    }

    // Test seam: custom catalog makes every run reproducible.
    public SkillProvider(List<LearnableSkill> skills) {
        super(skills);
    }

    private static ArrayList<LearnableSkill> buildDefaultCatalog() {
        ArrayList<LearnableSkill> skills = new ArrayList<LearnableSkill>();
        IdentityProvider identities = new IdentityProvider();
        // 10-hour run tuning: time gates cut from max 480min (80% of budget)
        // to max 110min, costs cut ~3x so a full profession path (~5-8k total)
        // is affordable in the first 2-3 hours.
        skills.add(new LearnableSkill("Typing Proficiency", "Improves speed on basic tasks.", 300, 0, makeList2(identities.createIdentity("Typist"), identities.createIdentity("Doctor"))));
        skills.add(new LearnableSkill("Basic Graphic Design", "Unlocks simple design jobs.", 800, 10, makeList1(identities.createIdentity("LogoDesigner"))));
        skills.add(new LearnableSkill("Content Writing", "Unlocks article writing jobs.", 1200, 25, makeList2(identities.createIdentity("Typist"), identities.createIdentity("Doctor"))));
        skills.add(new LearnableSkill("Web Development Basics", "Unlocks entry-level web tasks.", 2500, 45, makeList2(identities.createIdentity("Programmer"), identities.createIdentity("LogoDesigner"))));
        skills.add(new LearnableSkill("Advanced SEO", "Boosts earnings from web-related jobs.", 4000, 75, makeList1(identities.createIdentity("Programmer"))));
        skills.add(new LearnableSkill("Project Management", "Unlocks high-paying management tasks.", 8000, 110, makeList2(identities.createIdentity("Programmer"), identities.createIdentity("Doctor"))));
        return skills;
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

    public LearnableSkill getSkill(String name) {
        List<LearnableSkill> all = snapshot();
        for (int i = 0; i < all.size(); i++) {
            LearnableSkill skill = all.get(i);
            if (skill.getName().equals(name)) {
                return skill;
            }
        }
        return null;
    }

    public List<LearnableSkill> getSkillsForIdentity(Identity identity) {
        ArrayList<LearnableSkill> result = new ArrayList<LearnableSkill>();
        if (identity == null) {
            return result;
        }
        List<LearnableSkill> all = snapshot();
        for (int i = 0; i < all.size(); i++) {
            LearnableSkill skill = all.get(i);
            if (skill.isForIdentity(identity)) {
                result.add(skill);
            }
        }
        return result;
    }
}
