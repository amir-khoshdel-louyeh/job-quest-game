package services;

import model.player.ChallengePlayer;
import model.goals.Challenge;
import provider.ChallengeCatalog;
import java.util.ArrayList;
import java.util.List;

import utils.Lists;

// Hands out challenges and tracks their progress and rewards.
// The catalog is injected, so this rule layer never constructs its own copy.
public final class ChallengeService {
    // Reputation awarded for finishing a challenge.
    public static final int CHALLENGE_REPUTATION_REWARD = 2;

    private final ChallengeCatalog catalog;

    // ----- Construction -----
    // Every dependency is required; GameSession supplies the shared catalog.
    public ChallengeService(ChallengeCatalog catalog) {
        this.catalog = utils.Require.notNull(catalog, "challenge catalog");
    }

    // ----- Commands -----
    // Gives starting challenges to a new player.
    public void initializeChallenges(ChallengePlayer player) {
        if (player.getActiveChallenges().isEmpty()) {
            player.addChallenges(catalog.getChallenges());
        }
    }

    // Adds progress to every challenge of this type; rewards the newly completed ones.
    public List<Challenge> updateChallengeProgress(ChallengePlayer player,
                                                   Challenge.ChallengeType type, int amount) {
        ArrayList<Challenge> finished = new ArrayList<Challenge>();
        for (Challenge completed : player.updateChallengeProgress(type, amount)) {
            giveReward(player, completed);
            finished.add(completed);
        }
        return finished;
    }

    // ----- Private helpers -----
    // Gives money, XP and reputation for a completed challenge.
    private void giveReward(ChallengePlayer player, Challenge challenge) {
        player.deposit(challenge.getRewardMoney());
        player.addExperience(challenge.getRewardExperience());
        player.gainReputation(CHALLENGE_REPUTATION_REWARD);
    }

    // ----- Queries -----
    // Returns challenges that are not finished yet.
    public List<Challenge> getActiveIncompleteChallenges(ChallengePlayer player) {
        return Lists.filter(player.getActiveChallenges(), challenge -> !challenge.isCompleted());
    }

    // Returns challenges that are finished.
    public List<Challenge> getCompletedChallenges(ChallengePlayer player) {
        return Lists.filter(player.getActiveChallenges(), Challenge::isCompleted);
    }
}
