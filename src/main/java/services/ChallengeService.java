package services;

import model.player.User;
import model.goals.Challenge;
import java.util.ArrayList;
import java.util.List;
import provider.ChallengeProvider;

// Hands out challenges and tracks their progress and rewards.
public class ChallengeService {
    private ChallengeService() {
    }

    // Give starting challenges to a new user
    public static void initializeChallenges(User user) {
        if (user.getActiveChallenges().isEmpty()) {
            user.addChallenges(ChallengeProvider.getMainChallenges());
            refreshDailyChallenges(user);
        }
    }

    // Add daily challenges, remove old completed daily ones
    public static void refreshDailyChallenges(User user) {
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

    // Adds progress to every challenge of this type.
    public static List<Challenge> updateChallengeProgress(User user, Challenge.ChallengeType type, int amount) {
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
    private static void giveReward(User user, Challenge challenge) {
        user.deposit(challenge.getRewardMoney());
        boolean levelUp = user.addExperience(challenge.getRewardExperience());
        if (levelUp) {
            user.restoreFull();
        }
        user.addReputation(2);
    }

    // Returns challenges that are not finished yet.
    public static List<Challenge> getActiveIncompleteChallenges(User user) {
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
    public static List<Challenge> getCompletedChallenges(User user) {
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
