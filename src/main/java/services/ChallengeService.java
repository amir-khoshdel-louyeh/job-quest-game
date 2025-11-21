package services;

import model.player.User;
import model.goals.Challenge;
import java.util.ArrayList;
import java.util.List;

// Hands out challenges and tracks their progress and rewards.
// Instance (not static): challenge templates come from outside via Suppliers,
// so behavior is deterministic in tests and swappable per session (DIP) —
// same pattern as RoutineService.
public class ChallengeService {
    private final java.util.function.Supplier<List<Challenge>> dailySource;
    private final java.util.function.Supplier<List<Challenge>> mainSource;

    public ChallengeService() {
        provider.ChallengeProvider defaults = new provider.ChallengeProvider();
        this.dailySource = defaults::getDailyChallenges;
        this.mainSource = defaults::getMainChallenges;
    }

    // Test seam: fixed template lists make every run reproducible.
    public ChallengeService(java.util.function.Supplier<List<Challenge>> dailySource,
                            java.util.function.Supplier<List<Challenge>> mainSource) {
        if (dailySource == null || mainSource == null) {
            throw new IllegalArgumentException("challenge sources must not be null");
        }
        this.dailySource = dailySource;
        this.mainSource = mainSource;
    }

    // Give starting challenges to a new user
    public void initializeChallenges(User user) {
        if (user.getActiveChallenges().isEmpty()) {
            user.addChallenges(mainSource.get());
            refreshDailyChallenges(user);
        }
    }

    // Add daily challenges, remove old completed daily ones
    // Uses polymorphic copy(): no "new Challenge" switch here.
    public void refreshDailyChallenges(User user) {
        user.pruneCompletedDailyChallenges();
        List<Challenge> daily = dailySource.get();
        for (int i = 0; i < daily.size(); i++) {
            Challenge template = daily.get(i);
            if (!user.hasChallenge(template.getId())) {
                // fresh copy so progress is separate, same runtime type
                user.addChallenge(template.copy());
            }
        }
    }

    // Adds progress to every challenge of this type.
    // Mutation lives inside User/ChallengeBook; the service only hands out
    // rewards for the newly completed ones (no direct addProgress here,
    // so callers cannot forge progress via a leaked list reference).
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

    // Give money, XP and reputation for completed challenge
    private void giveReward(User user, Challenge challenge) {
        user.deposit(challenge.getRewardMoney());
        user.addExperience(challenge.getRewardExperience());
        user.addReputation(2);
    }

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
