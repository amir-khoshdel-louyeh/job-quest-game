package provider;

import java.util.ArrayList;
import java.util.List;

import model.Quest;

/**
 * Provides available quests for the game.
 */
public class QuestProvider {
    
    private static final List<Quest> DAILY_QUESTS = new ArrayList<>();
    private static final List<Quest> WEEKLY_QUESTS = new ArrayList<>();
    private static final List<Quest> MAIN_QUESTS = new ArrayList<>();
    
    static {
        // Daily Quests (reset daily)
        DAILY_QUESTS.add(new Quest(
            "daily_jobs_3",
            "Daily Hustle",
            "Complete 3 jobs today",
            Quest.QuestType.COMPLETE_JOBS,
            3, 500, 100
        ));
        
        DAILY_QUESTS.add(new Quest(
            "daily_earn_1000",
            "Daily Earnings",
            "Earn $1,000 today",
            Quest.QuestType.EARN_MONEY,
            1000, 300, 50
        ));
        
        // Weekly Quests
        WEEKLY_QUESTS.add(new Quest(
            "weekly_jobs_20",
            "Weekly Grind",
            "Complete 20 jobs this week",
            Quest.QuestType.COMPLETE_JOBS,
            20, 3000, 500
        ));
        
        WEEKLY_QUESTS.add(new Quest(
            "weekly_skill",
            "Skill Development",
            "Learn 2 new skills this week",
            Quest.QuestType.LEARN_SKILLS,
            2, 2000, 400
        ));
        
        WEEKLY_QUESTS.add(new Quest(
            "weekly_health",
            "Stay Healthy",
            "Maintain health above 80 for 5 days",
            Quest.QuestType.MAINTAIN_HEALTH,
            5, 1500, 300
        ));
        
        // Main Story Quests
        MAIN_QUESTS.add(new Quest(
            "main_first_job",
            "First Steps",
            "Complete your first job",
            Quest.QuestType.COMPLETE_JOBS,
            1, 200, 50
        ));
        
        MAIN_QUESTS.add(new Quest(
            "main_beginner",
            "Getting Started",
            "Complete 10 jobs",
            Quest.QuestType.COMPLETE_JOBS,
            10, 1000, 200
        ));
        
        MAIN_QUESTS.add(new Quest(
            "main_first_thousand",
            "First Thousand",
            "Earn $1,000 in total",
            Quest.QuestType.EARN_MONEY,
            1000, 500, 100
        ));
        
        MAIN_QUESTS.add(new Quest(
            "main_skilled_worker",
            "Skilled Worker",
            "Learn 3 different skills",
            Quest.QuestType.LEARN_SKILLS,
            3, 1500, 300
        ));
        
        MAIN_QUESTS.add(new Quest(
            "main_professional",
            "Professional",
            "Complete 50 jobs",
            Quest.QuestType.COMPLETE_JOBS,
            50, 5000, 1000
        ));
    }
    
    public static List<Quest> getDailyQuests() {
        return new ArrayList<>(DAILY_QUESTS);
    }
    
    public static List<Quest> getWeeklyQuests() {
        return new ArrayList<>(WEEKLY_QUESTS);
    }
    
    public static List<Quest> getMainQuests() {
        return new ArrayList<>(MAIN_QUESTS);
    }
}
