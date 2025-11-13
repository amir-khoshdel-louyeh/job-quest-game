package services;

import model.Challenge;
import model.User;
import java.util.ArrayList;
import java.util.List;
import provider.ChallengeProvider;

// This class manages challenges for the player.
public class ChallengeService {
    // only one object of this class (simple singleton)
    private static ChallengeService instance;

    // private constructor so nobody can create with new
    private ChallengeService() {
    }

    // Return the single object, create it if needed
    public static ChallengeService getInstance() {
        if (instance == null) {
            instance = new ChallengeService();
        }
        return instance;
    }

    // Give starting challenges to a new user
    public void initializeChallenges(User user) {
        if (user.getActiveChallenges().isEmpty()) {
            user.addChallenges(ChallengeProvider.getMainChallenges());
            refreshDailyChallenges(user);
        }
    }

    // Add daily challenges, remove old completed daily ones
    public void refreshDailyChallenges(User user) {
        user.pruneCompletedDailyChallenges();
        List<Challenge> daily = ChallengeProvider.getDailyChallenges();
        for (int i = 0; i < daily.size(); i++) {
            Challenge template = daily.get(i);
            if (!user.hasChallenge(template.getId())) {
                // make a fresh copy so progress is separate
                Challenge copy = new Challenge(
                    template.getId(),
                    template.getName(),
                    template.getDescription(),
                    template.getType(),
                    template.getTargetValue(),
                    template.getRewardMoney(),
                    template.getRewardExperience()
                );
                user.addChallenge(copy);
            }
        }
    }

    // Add progress to all challenges of this type, uses simple for loop
    public List<Challenge> updateChallengeProgress(User user, Challenge.ChallengeType type, int amount) {
        ArrayList<Challenge> finished = new ArrayList<Challenge>();
        List<Challenge> active = user.getActiveChallenges();
        for (int i = 0; i < active.size(); i++) {
            Challenge c = active.get(i);
            if (c.getType() == type && !c.isCompleted()) {
                c.addProgress(amount);
                if (c.isCompleted()) {
                    giveReward(user, c);
                    finished.add(c);
                }
            }
        }
        return finished;
    }

    // Give money, XP and reputation for completed challenge
    private void giveReward(User user, Challenge challenge) {
        user.deposit(challenge.getRewardMoney());
        user.addExperience(challenge.getRewardExperience());
        user.addReputation(2);
    }

    // Return challenges that are not finished yet, uses for loop (no stream)
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

    // Return challenges that are finished, uses for loop (no stream)
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
