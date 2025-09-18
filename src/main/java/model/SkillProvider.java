package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Retrieves a skill by its unique name.
     * @param name The name of the skill.
     * @return The LearnableSkill object, or null if not found.
     */
    public static LearnableSkill getSkill(String name) {
        return ALL_SKILLS.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Gets a list of skills that the user is eligible to learn based on their playtime
     * and that they haven't learned already.
     * @param user The user to check against.
     * @return A list of available skills to learn.
     */
    public static List<LearnableSkill> getAvailableSkillsForUser(User user) {
        long userPlayTimeMinutes = user.getTotalPlayTime() / (1000 * 60);
        List<String> userLearnedSkills = user.getSkills().stream().map(Skill::getName).toList();

        return ALL_SKILLS.stream()
                .filter(skill -> userPlayTimeMinutes >= skill.getRequiredPlayTimeMinutes()) // Check playtime
                .filter(skill -> !userLearnedSkills.contains(skill.getName())) // Check if already learned
                .collect(Collectors.toList());
    }
}