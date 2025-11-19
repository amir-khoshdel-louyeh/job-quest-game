package provider;

import java.util.ArrayList;
import java.util.List;

import model.goals.Challenge;

// Holds the daily and story challenges the player can try.
// One concrete Challenge class keyed by ChallengeType: the old per-type
// subclasses carried no behavior (pure type tags duplicating the enum),
// so they were collapsed. New kinds add an enum value + entries here;
// progress dispatch compares enum values, never switches (OCP).
// Note: every ChallengeType has both a producer (an entry below) and a
// consumer (an updateChallengeProgress call) — no dead types.
public class ChallengeProvider {

    private static final List<Challenge> DAILY_CHALLENGES = new ArrayList<>();
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
            "Complete 40 jobs",
            Challenge.ChallengeType.COMPLETE_JOBS,
            40, 5000, 1000
        ));
    }

    public static List<Challenge> getDailyChallenges() {
        return new ArrayList<>(DAILY_CHALLENGES);
    }

    public static List<Challenge> getMainChallenges() {
        return new ArrayList<>(MAIN_CHALLENGES);
    }
}
