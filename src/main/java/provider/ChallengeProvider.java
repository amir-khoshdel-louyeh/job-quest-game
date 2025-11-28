package provider;

import java.util.ArrayList;
import java.util.List;

import model.goals.Challenge;

// Holds the story challenges the player can try.
// Injected into ChallengeService.
public class ChallengeProvider {

    private final List<Challenge> challenges;

    // ----- Construction -----
    public ChallengeProvider() {
        this(buildCatalog());
    }

    // Custom catalog for tests.
    public ChallengeProvider(List<Challenge> challenges) {
        if (challenges == null) {
            throw new IllegalArgumentException("challenge catalog must not be null");
        }
        this.challenges = new ArrayList<>(challenges);
    }

    private static List<Challenge> buildCatalog() {
        List<Challenge> all = new ArrayList<>();
        all.add(new Challenge(
            "hustle_3",
            "Hustle",
            "Complete 3 jobs",
            Challenge.ChallengeType.COMPLETE_JOBS,
            3, 500, 100
        ));

        all.add(new Challenge(
            "earn_1000",
            "Earnings",
            "Earn $1,000",
            Challenge.ChallengeType.EARN_MONEY,
            1000, 300, 50
        ));

        all.add(new Challenge(
            "main_first_job",
            "First Steps",
            "Complete your first job",
            Challenge.ChallengeType.COMPLETE_JOBS,
            1, 200, 50
        ));

        all.add(new Challenge(
            "main_beginner",
            "Getting Started",
            "Complete 10 jobs",
            Challenge.ChallengeType.COMPLETE_JOBS,
            10, 1000, 200
        ));

        all.add(new Challenge(
            "main_first_thousand",
            "First Thousand",
            "Earn $1,000 in total",
            Challenge.ChallengeType.EARN_MONEY,
            1000, 500, 100
        ));

        all.add(new Challenge(
            "main_skilled_worker",
            "Skilled Worker",
            "Learn 3 different skills",
            Challenge.ChallengeType.LEARN_SKILLS,
            3, 1500, 300
        ));

        all.add(new Challenge(
            "main_professional",
            "Professional",
            "Complete 40 jobs",
            Challenge.ChallengeType.COMPLETE_JOBS,
            40, 5000, 1000
        ));
        return all;
    }

    // ----- Getter -----
    public List<Challenge> getChallenges() {
        return new ArrayList<>(challenges);
    }
}
