package provider;

import java.util.ArrayList;
import java.util.List;

import model.LearnableSkill;
import model.Skill;
import model.User;

/**
 * Provides a centralized catalog of all learnable skills in the game.
 */
public class SkillProvider {

    private static final List<LearnableSkill> ALL_SKILLS = new ArrayList<>();

    static {
        // To add a new skill, just add a new line here.
        // Playtime is in minutes. Last arg = owning professions (school + jobs
        // only ever show a character the skills of its own profession).
        // Shared foundations list both owners (e.g. Content Writing -> Typist, Doctor).
        ALL_SKILLS.add(new LearnableSkill("Typing Proficiency", "Improves speed on basic tasks.", 500, 0, List.of("Typist", "Doctor"))); // Available from start
        ALL_SKILLS.add(new LearnableSkill("Basic Graphic Design", "Unlocks simple design jobs.", 1500, 30, List.of("LogoDesigner"))); // 30 minutes playtime
        ALL_SKILLS.add(new LearnableSkill("Content Writing", "Unlocks article writing jobs.", 2000, 60, List.of("Typist", "Doctor"))); // 1 hour playtime
        ALL_SKILLS.add(new LearnableSkill("Web Development Basics", "Unlocks entry-level web tasks.", 5000, 120, List.of("Programmer", "LogoDesigner"))); // 2 hours playtime
        ALL_SKILLS.add(new LearnableSkill("Advanced SEO", "Boosts earnings from web-related jobs.", 10000, 240, List.of("Programmer"))); // 4 hours playtime
        ALL_SKILLS.add(new LearnableSkill("Project Management", "Unlocks high-paying management tasks.", 25000, 480, List.of("Programmer", "Doctor"))); // 8 hours playtime
    }

    /** Get a learnable skill by its name */
    public static LearnableSkill getSkill(String name) {
        // find a learnable skill by name
        for (LearnableSkill skill : ALL_SKILLS) {
            if (skill.getName().equals(name)) {
                return skill;
            }
        }
        return null;
    }

    /** Get the full skill catalog (available + locked + learned) for browsing. */
    public static List<LearnableSkill> getAllSkills() {
        return java.util.Collections.unmodifiableList(ALL_SKILLS);
    }

    /**
     * Get the school lessons of one profession only (identity simple name,
     * e.g. "Doctor"). A character never sees another profession's lessons.
     */
    public static List<LearnableSkill> getSkillsForIdentity(String identitySimpleName) {
        List<LearnableSkill> result = new ArrayList<>();
        for (LearnableSkill skill : ALL_SKILLS) {
            if (skill.isForIdentity(identitySimpleName)) {
                result.add(skill);
            }
        }
        return result;
    }

    /**
     * OOP Step 1: object-based overload — pass the Identity object.
     */
    public static List<LearnableSkill> getSkillsForIdentity(model.Identity identity) {
        if (identity == null) return List.of();
        List<LearnableSkill> result = new ArrayList<>();
        for (LearnableSkill skill : ALL_SKILLS) {
            if (skill.isForIdentity(identity)) {
                result.add(skill);
            }
        }
        return result;
    }

    /** Get list of learnable skills available for the user (own profession only) */
    public static List<LearnableSkill> getAvailableSkillsForUser(User user) {
        if (user == null || user.getIdentity() == null) return List.of();
        long userPlayTimeMinutes = user.getTotalPlayTime() / (1000 * 60);

        List<LearnableSkill> availableSkills = new ArrayList<>();
        for (LearnableSkill skill : ALL_SKILLS) {
            if (!skill.isForIdentity(user.getIdentity())) continue;
            if (userPlayTimeMinutes >= skill.getRequiredPlayTimeMinutes() &&
                !user.hasSkill(skill.getName())) {
                availableSkills.add(skill);
            }
        }
        return availableSkills;
    }
}
