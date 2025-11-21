package provider;

import java.util.ArrayList;
import java.util.List;

import model.goals.Challenge;

// Holds the daily and story challenges the player can try.
// Instance repository (not static holder): the catalog comes from outside,
// so ChallengeService can inject/mock it (DIP) — same pattern as
// Job/Skill providers. Daily-ness is explicit data on Challenge,
// not an id-prefix convention.
// Note: every ChallengeType has both a producer (an entry below) and a
// consumer (an updateChallengeProgress call) — no dead types.
public class ChallengeProvider {

    private final List<Challenge> dailyChallenges;
    private final List<Challenge> mainChallenges;

    public ChallengeProvider() {
        this(buildDailyCatalog(), buildMainCatalog());
    }

    // Test seam: custom catalogs make every run reproducible.
    public ChallengeProvider(List<Challenge> daily, List<Challenge> main) {
        if (daily == null || main == null) {
            throw new IllegalArgumentException("challenge catalogs must not be null");
        }
        this.dailyChallenges = new ArrayList<>(daily);
        this.mainChallenges = new ArrayList<>(main);
    }

    private static List<Challenge> buildDailyCatalog() {
        List<Challenge> daily = new ArrayList<>();
        // Daily Challenges (reset daily)
        daily.add(new Challenge(
            "daily_jobs_3",
            "Daily Hustle",
            "Complete 3 jobs today",
            Challenge.ChallengeType.COMPLETE_JOBS,
            3, 500, 100, true
        ));

        daily.add(new Challenge(
            "daily_earn_1000",
            "Daily Earnings",
            "Earn $1,000 today",
            Challenge.ChallengeType.EARN_MONEY,
            1000, 300, 50, true
        ));
        return daily;
    }

    private static List<Challenge> buildMainCatalog() {
        List<Challenge> main = new ArrayList<>();
        // Main Story Challenges
        main.add(new Challenge(
            "main_first_job",
            "First Steps",
            "Complete your first job",
            Challenge.ChallengeType.COMPLETE_JOBS,
            1, 200, 50, false
        ));

        main.add(new Challenge(
            "main_beginner",
            "Getting Started",
            "Complete 10 jobs",
            Challenge.ChallengeType.COMPLETE_JOBS,
            10, 1000, 200, false
        ));

        main.add(new Challenge(
            "main_first_thousand",
            "First Thousand",
            "Earn $1,000 in total",
            Challenge.ChallengeType.EARN_MONEY,
            1000, 500, 100, false
        ));

        main.add(new Challenge(
            "main_skilled_worker",
            "Skilled Worker",
            "Learn 3 different skills",
            Challenge.ChallengeType.LEARN_SKILLS,
            3, 1500, 300, false
        ));

        main.add(new Challenge(
            "main_professional",
            "Professional",
            "Complete 40 jobs",
            Challenge.ChallengeType.COMPLETE_JOBS,
            40, 5000, 1000, false
        ));
        return main;
    }

    public List<Challenge> getDailyChallenges() {
        return new ArrayList<>(dailyChallenges);
    }

    public List<Challenge> getMainChallenges() {
        return new ArrayList<>(mainChallenges);
    }
}
