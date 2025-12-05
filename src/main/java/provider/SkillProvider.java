package provider;

import provider.contract.SkillCatalog;

import java.util.ArrayList;
import java.util.List;

import model.career.Profession;
import model.career.Skill;
import utils.Lists;
import utils.Require;

// Holds all skills the player can learn at school.
// Extends AbstractCatalog; injected into SkillController.
public final class SkillProvider extends AbstractCatalog<Skill> implements SkillCatalog {

    // ----- Construction -----
    // The profession registry comes from the composition root; this catalog
    // never builds a second copy of it.
    public SkillProvider(ProfessionRegistry professions) {
        super(buildDefaultCatalog(Require.notNull(professions, "professions")));
    }

    // Tuned for a 10-hour run: full path affordable in the first hours.
    private static ArrayList<Skill> buildDefaultCatalog(ProfessionRegistry professions) {
        ArrayList<Skill> skills = new ArrayList<Skill>();
        skills.add(new Skill("Typing Proficiency", "Improves speed on basic tasks.", 300, 0, makeList2(professions.createProfession("Typist"), professions.createProfession("Doctor"))));
        skills.add(new Skill("Basic Graphic Design", "Unlocks simple design jobs.", 800, 10, makeList1(professions.createProfession("LogoDesigner"))));
        skills.add(new Skill("Content Writing", "Unlocks article writing jobs.", 1200, 25, makeList2(professions.createProfession("Typist"), professions.createProfession("Doctor"))));
        skills.add(new Skill("Web Development Basics", "Unlocks entry-level web tasks.", 2500, 45, makeList2(professions.createProfession("Programmer"), professions.createProfession("LogoDesigner"))));
        skills.add(new Skill("Advanced SEO", "Boosts earnings from web-related jobs.", 4000, 75, makeList1(professions.createProfession("Programmer"))));
        skills.add(new Skill("Project Management", "Unlocks high-paying management tasks.", 8000, 110, makeList2(professions.createProfession("Programmer"), professions.createProfession("Doctor"))));
        return skills;
    }

    // ----- Private helpers -----
    private static ArrayList<Profession> makeList1(Profession i1) {
        ArrayList<Profession> list = new ArrayList<Profession>();
        list.add(i1);
        return list;
    }

    private static ArrayList<Profession> makeList2(Profession i1, Profession i2) {
        ArrayList<Profession> list = new ArrayList<Profession>();
        list.add(i1);
        list.add(i2);
        return list;
    }

    // ----- Template hook -----
    @Override
    protected String nameOf(Skill entry) {
        return entry.getName();
    }

    // ----- Queries -----
    public List<Skill> getSkillsForProfession(Profession profession) {
        if (profession == null) {
            return new ArrayList<Skill>();
        }
        return Lists.filter(snapshot(), skill -> skill.isForProfession(profession));
    }
}
