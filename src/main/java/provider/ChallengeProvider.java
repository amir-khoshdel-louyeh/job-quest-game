package provider;

import provider.contract.ChallengeCatalog;

import java.util.ArrayList;
import java.util.List;

import model.goals.Challenge;
import model.goals.ChallengeType;

// Holds the story challenges the player can try.
// Extends AbstractCatalog; Challenge carries per-player progress, so every
// read hands out a clone (see copyOf).
public final class ChallengeProvider extends AbstractCatalog<Challenge> implements ChallengeCatalog {

    // ----- Construction -----
    public ChallengeProvider() {
        super(buildCatalog());
    }

    private static List<Challenge> buildCatalog() {
        List<Challenge> all = new ArrayList<>();
        all.add(new Challenge(
            "hustle_3",
            "Hustle",
            "Complete 3 jobs",
            ChallengeType.COMPLETE_JOBS,
            3, 500, 100
        ));

        all.add(new Challenge(
            "earn_1000",
            "Earnings",
            "Earn $1,000",
            ChallengeType.EARN_MONEY,
            1000, 300, 50
        ));

        all.add(new Challenge(
            "main_first_job",
            "First Steps",
            "Complete your first job",
            ChallengeType.COMPLETE_JOBS,
            1, 200, 50
        ));

        all.add(new Challenge(
            "main_beginner",
            "Getting Started",
            "Complete 10 jobs",
            ChallengeType.COMPLETE_JOBS,
            10, 1000, 200
        ));

        all.add(new Challenge(
            "main_first_thousand",
            "First Thousand",
            "Earn $1,000 in total",
            ChallengeType.EARN_MONEY,
            1000, 500, 100
        ));

        all.add(new Challenge(
            "main_skilled_worker",
            "Skilled Worker",
            "Learn 3 different skills",
            ChallengeType.LEARN_SKILLS,
            3, 1500, 300
        ));

        all.add(new Challenge(
            "main_professional",
            "Professional",
            "Complete 40 jobs",
            ChallengeType.COMPLETE_JOBS,
            40, 5000, 1000
        ));
        return all;
    }

    // ----- Template hooks -----
    @Override
    protected String nameOf(Challenge entry) {
        return entry.getName();
    }

    // Challenge is mutable, so a catalog must never hand out the instances it
    // stores (two players would share progress).
    @Override
    protected Challenge copyOf(Challenge entry) {
        return entry.copy();
    }
}
