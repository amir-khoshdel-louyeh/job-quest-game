package services;

import model.player.User;
import model.goals.Challenge;
import java.util.ArrayList;
import java.util.List;

// Hands out challenges and tracks their progress and rewards.
// Templates come from outside; injected into GameController.
public class ChallengeService {
    private final java.util.function.Supplier<List<Challenge>> challengeSource;

    // ----- Construction -----
    public ChallengeService() {
        provider.ChallengeProvider defaults = new provider.ChallengeProvider();
        this.challengeSource = defaults::getChallenges;
    }

    // Custom template list for tests.
    public ChallengeService(java.util.function.Supplier<List<Challenge>> challengeSource) {
        if (challengeSource == null) {
            throw new IllegalArgumentException("challenge source must not be null");
        }
        this.challengeSource = challengeSource;
    }

    // ----- Commands -----
    // Gives starting challenges to a new user.
    public void initializeChallenges(User user) {
        if (user.getActiveChallenges().isEmpty()) {
            user.addChallenges(challengeSource.get());
        }
    }

    // Adds progress to every challenge of this type; rewards the newly completed ones.
    public List<Challenge> updateChallengeProgress(User user, Challenge.ChallengeType type, int amount) {
        ArrayList<Challenge> finished = new ArrayList<Challenge>();
        List<Challenge> newlyCompleted = user.updateChallengeProgress(type, amount);
        for (int i = 0; i < newlyCompleted.size(); i++) {
            Challenge c = newlyCompleted.get(i);
            giveReward(user, c);
            finished.add(c);
        }
        return finished;
    }

    // ----- Private helpers -----
    // Gives money, XP and reputation for a completed challenge.
    private void giveReward(User user, Challenge challenge) {
        user.deposit(challenge.getRewardMoney());
        user.addExperience(challenge.getRewardExperience());
        user.gainReputation(2);
    }

    // ----- Queries -----
    // Returns challenges that are not finished yet.
    public List<Challenge> getActiveIncompleteChallenges(User user) {
        ArrayList<Challenge> result = new ArrayList<Challenge>();
        List<Challenge> active = user.getActiveChallenges();
        for (int i = 0; i < active.size(); i++) {
            Challenge c = active.get(i);
            if (!c.isCompleted()) {
                result.add(c);
            }
        }
        return result;
    }

    // Returns challenges that are finished.
    public List<Challenge> getCompletedChallenges(User user) {
        ArrayList<Challenge> result = new ArrayList<Challenge>();
        List<Challenge> active = user.getActiveChallenges();
        for (int i = 0; i < active.size(); i++) {
            Challenge c = active.get(i);
            if (c.isCompleted()) {
                result.add(c);
            }
        }
        return result;
    }
}
