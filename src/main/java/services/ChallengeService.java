package services;

import model.goals.Challenge;
import model.goals.ChallengeType;
import model.player.PlayerProgress;
import provider.ChallengeCatalog;
import java.util.ArrayList;
import java.util.List;

import utils.Lists;

// Hands out challenges and tracks their progress and rewards.
// The catalog is injected, so this rule layer never constructs its own copy.
public final class ChallengeService implements ChallengeRules {
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
    @Override
    public void initializeChallenges(PlayerProgress player) {
        if (player.getActiveChallenges().isEmpty()) {
            player.addChallenges(catalog.getChallenges());
        }
    }

    // Adds progress to every challenge of this type; rewards the newly completed ones.
    @Override
    public List<Challenge> updateChallengeProgress(PlayerProgress player,
                                                   ChallengeType type, int amount) {
        ArrayList<Challenge> finished = new ArrayList<Challenge>();
        for (Challenge completed : player.updateChallengeProgress(type, amount)) {
            giveReward(player, completed);
            finished.add(completed);
        }
        return finished;
    }

    // ----- Private helpers -----
    // Gives money, XP and reputation for a completed challenge.
    private void giveReward(PlayerProgress player, Challenge challenge) {
        player.deposit(challenge.getRewardMoney());
        player.addExperience(challenge.getRewardExperience());
        player.gainReputation(CHALLENGE_REPUTATION_REWARD);
    }

    // ----- Queries -----
    // Returns challenges that are not finished yet.
    @Override
    public List<Challenge> getActiveIncompleteChallenges(PlayerProgress player) {
        return Lists.filter(player.getActiveChallenges(), challenge -> !challenge.isCompleted());
    }

    // Returns challenges that are finished.
    @Override
    public List<Challenge> getCompletedChallenges(PlayerProgress player) {
        return Lists.filter(player.getActiveChallenges(), Challenge::isCompleted);
    }
}
