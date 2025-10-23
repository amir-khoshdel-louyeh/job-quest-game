package provider;

import java.util.ArrayList;
import java.util.List;

import model.Challenge;

/**
 * Provides available challenges for the game.
 */
public class ChallengeProvider {
    
    private static final List<Challenge> DAILY_CHALLENGES = new ArrayList<>();
    private static final List<Challenge> WEEKLY_CHALLENGES = new ArrayList<>();
    private static final List<Challenge> MAIN_CHALLENGES = new ArrayList<>();
    
    static {
        // Daily Challenges (reset daily)
        DAILY_CHALLENGES.add(new Challenge(
            "daily_jobs_3",
            "Daily Hustle",
            "Complete 3 jobs today",
            Challenge.ChallengeType.COMPLETE_JOBS,
            3, 500, 100
        ));
        
        DAILY_CHALLENGES.add(new Challenge(
            "daily_earn_1000",
            "Daily Earnings",
            "Earn $1,000 today",
            Challenge.ChallengeType.EARN_MONEY,
            1000, 300, 50
        ));
        
        // Weekly Challenges
        WEEKLY_CHALLENGES.add(new Challenge(
            "weekly_jobs_20",
            "Weekly Grind",
            "Complete 20 jobs this week",
            Challenge.ChallengeType.COMPLETE_JOBS,
            20, 3000, 500
        ));
        
        WEEKLY_CHALLENGES.add(new Challenge(
            "weekly_skill",
            "Skill Development",
            "Learn 2 new skills this week",
            Challenge.ChallengeType.LEARN_SKILLS,
            2, 2000, 400
        ));
        
        WEEKLY_CHALLENGES.add(new Challenge(
            "weekly_health",
            "Stay Healthy",
            "Maintain health above 80 for 5 days",
            Challenge.ChallengeType.MAINTAIN_HEALTH,
            5, 1500, 300
        ));
        
        // Main Story Challenges
        MAIN_CHALLENGES.add(new Challenge(
            "main_first_job",
            "First Steps",
            "Complete your first job",
            Challenge.ChallengeType.COMPLETE_JOBS,
            1, 200, 50
        ));
        
        MAIN_CHALLENGES.add(new Challenge(
            "main_beginner",
            "Getting Started",
            "Complete 10 jobs",
            Challenge.ChallengeType.COMPLETE_JOBS,
            10, 1000, 200
        ));
        
        MAIN_CHALLENGES.add(new Challenge(
            "main_first_thousand",
            "First Thousand",
            "Earn $1,000 in total",
            Challenge.ChallengeType.EARN_MONEY,
            1000, 500, 100
        ));
        
        MAIN_CHALLENGES.add(new Challenge(
            "main_skilled_worker",
            "Skilled Worker",
            "Learn 3 different skills",
            Challenge.ChallengeType.LEARN_SKILLS,
            3, 1500, 300
        ));
        
        MAIN_CHALLENGES.add(new Challenge(
            "main_professional",
            "Professional",
            "Complete 50 jobs",
            Challenge.ChallengeType.COMPLETE_JOBS,
            50, 5000, 1000
        ));
    }
    
    public static List<Challenge> getDailyChallenges() {
        return new ArrayList<>(DAILY_CHALLENGES);
    }
    
    public static List<Challenge> getWeeklyChallenges() {
        return new ArrayList<>(WEEKLY_CHALLENGES);
    }
    
    public static List<Challenge> getMainChallenges() {
        return new ArrayList<>(MAIN_CHALLENGES);
    }
}
