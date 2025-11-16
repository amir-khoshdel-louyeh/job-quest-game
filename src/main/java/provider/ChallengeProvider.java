package provider;

import java.util.ArrayList;
import java.util.List;

import model.goals.Challenge;
import model.goals.CompleteJobsChallenge;
import model.goals.EarnMoneyChallenge;
import model.goals.LearnSkillsChallenge;
import model.goals.MaintainHealthChallenge;

// Holds the daily, weekly, and story challenges the player can try.
// Uses Challenge subclasses (not raw "new Challenge"): new kinds
// extend the hierarchy instead of editing switches (OCP).
public class ChallengeProvider {

    private static final List<Challenge> DAILY_CHALLENGES = new ArrayList<>();
    private static final List<Challenge> WEEKLY_CHALLENGES = new ArrayList<>();
    private static final List<Challenge> MAIN_CHALLENGES = new ArrayList<>();

    static {
        // Daily Challenges (reset daily)
        DAILY_CHALLENGES.add(new CompleteJobsChallenge(
            "daily_jobs_3",
            "Daily Hustle",
            "Complete 3 jobs today",
            3, 500, 100
        ));

        DAILY_CHALLENGES.add(new EarnMoneyChallenge(
            "daily_earn_1000",
            "Daily Earnings",
            "Earn $1,000 today",
            1000, 300, 50
        ));

        // Weekly Challenges (10-hour run tuning: 20 -> 12 jobs so a weekly
        // fits in one long session; rewards unchanged)
        WEEKLY_CHALLENGES.add(new CompleteJobsChallenge(
            "weekly_jobs_20",
            "Weekly Grind",
            "Complete 12 jobs this week",
            12, 3000, 500
        ));

        WEEKLY_CHALLENGES.add(new LearnSkillsChallenge(
            "weekly_skill",
            "Skill Development",
            "Learn 2 new skills this week",
            2, 2000, 400
        ));

        WEEKLY_CHALLENGES.add(new MaintainHealthChallenge(
            "weekly_health",
            "Stay Healthy",
            "Maintain health above 80 for 5 days",
            5, 1500, 300
        ));

        // Main Story Challenges
        MAIN_CHALLENGES.add(new CompleteJobsChallenge(
            "main_first_job",
            "First Steps",
            "Complete your first job",
            1, 200, 50
        ));

        MAIN_CHALLENGES.add(new CompleteJobsChallenge(
            "main_beginner",
            "Getting Started",
            "Complete 10 jobs",
            10, 1000, 200
        ));

        MAIN_CHALLENGES.add(new EarnMoneyChallenge(
            "main_first_thousand",
            "First Thousand",
            "Earn $1,000 in total",
            1000, 500, 100
        ));

        MAIN_CHALLENGES.add(new LearnSkillsChallenge(
            "main_skilled_worker",
            "Skilled Worker",
            "Learn 3 different skills",
            3, 1500, 300
        ));

        MAIN_CHALLENGES.add(new CompleteJobsChallenge(
            "main_professional",
            "Professional",
            "Complete 40 jobs",
            40, 5000, 1000
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
