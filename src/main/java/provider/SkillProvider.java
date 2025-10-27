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
        // Playtime is in minutes.
        ALL_SKILLS.add(new LearnableSkill("Typing Proficiency", "Improves speed on basic tasks.", 500, 0)); // Available from start
        ALL_SKILLS.add(new LearnableSkill("Basic Graphic Design", "Unlocks simple design jobs.", 1500, 30)); // 30 minutes playtime
        ALL_SKILLS.add(new LearnableSkill("Content Writing", "Unlocks article writing jobs.", 2000, 60)); // 1 hour playtime
        ALL_SKILLS.add(new LearnableSkill("Web Development Basics", "Unlocks entry-level web tasks.", 5000, 120)); // 2 hours playtime
        ALL_SKILLS.add(new LearnableSkill("Advanced SEO", "Boosts earnings from web-related jobs.", 10000, 240)); // 4 hours playtime
        ALL_SKILLS.add(new LearnableSkill("Project Management", "Unlocks high-paying management tasks.", 25000, 480)); // 8 hours playtime
    }

    /** دریافت مهارت بر اساس نام */
    public static LearnableSkill getSkill(String name) {
        for (LearnableSkill skill : ALL_SKILLS) {
            if (skill.getName().equals(name)) {
                return skill;
            }
        }
        return null;
    }

    /** دریافت لیست مهارت‌های قابل یادگیری برای کاربر */
    public static List<LearnableSkill> getAvailableSkillsForUser(User user) {
        long userPlayTimeMinutes = user.getTotalPlayTime() / (1000 * 60);
        List<String> userLearnedSkills = new ArrayList<>();
        for (Skill s : user.getSkills()) {
            userLearnedSkills.add(s.getName());
        }

        List<LearnableSkill> availableSkills = new ArrayList<>();
        for (LearnableSkill skill : ALL_SKILLS) {
            if (userPlayTimeMinutes >= skill.getRequiredPlayTimeMinutes() &&
                !userLearnedSkills.contains(skill.getName())) {
                availableSkills.add(skill);
            }
        }
        return availableSkills;
    }
}
