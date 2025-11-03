package services;

import model.Challenge;
import model.User;
import java.util.ArrayList;
import java.util.List;
import provider.ChallengeProvider;

/**
 * Manages challenges and tracks player progress.
 */
public class ChallengeService {
    private static ChallengeService instance;
    
    private ChallengeService() {}
    // private constructor for singleton
    
    public static ChallengeService getInstance() {
        if (instance == null) {
            instance = new ChallengeService();
        }
        return instance;
    }
    
    // initialize main and daily challenges for a new user
    public void initializeChallenges(User user) {
        if (user.getActiveChallenges().isEmpty()) {
            user.getActiveChallenges().addAll(provider.ChallengeProvider.getMainChallenges());
            refreshDailyChallenges(user);
        }
    }
    
    // refresh daily challenges for the user (call daily)
    public void refreshDailyChallenges(User user) {
        // Remove completed daily challenges
        user.getActiveChallenges().removeIf(q -> 
            q.getId().startsWith("daily_") && q.isCompleted()
        );
            for (Challenge dailyChallenge : ChallengeProvider.getDailyChallenges()) {
            boolean alreadyActive = user.getActiveChallenges().stream()
                .anyMatch(q -> q.getId().equals(dailyChallenge.getId()));
            
            if (!alreadyActive) {
                user.addChallenge(new Challenge(
                    dailyChallenge.getId(),
                    dailyChallenge.getName(),
                    dailyChallenge.getDescription(),
                    dailyChallenge.getType(),
                    dailyChallenge.getTargetValue(),
                    dailyChallenge.getRewardMoney(),
                    dailyChallenge.getRewardExperience()
                ));
            }
        }
    }
    
    // update challenge progress by type and amount
    public List<Challenge> updateChallengeProgress(User user, Challenge.ChallengeType type, int amount) {
        List<Challenge> completedChallenges = new ArrayList<>();
        
        for (Challenge challenge : user.getActiveChallenges()) {
            if (challenge.getType() == type && !challenge.isCompleted()) {
                challenge.addProgress(amount);
                if (challenge.isCompleted()) {
                    grantChallengeReward(user, challenge);
                    completedChallenges.add(challenge);
                }
            }
        }
        
        return completedChallenges;
    }
    
    // grant money/XP/reputation rewards for a completed challenge
    private void grantChallengeReward(User user, Challenge challenge) {
    user.deposit(challenge.getRewardMoney());
        user.addExperience(challenge.getRewardExperience());
        user.addReputation(2); // Challenges give reputation
    }
    public List<Challenge> getActiveIncompleteChallenges(User user) {
        return user.getActiveChallenges().stream()
            .filter(q -> !q.isCompleted())
            .toList();
    }
    public List<Challenge> getCompletedChallenges(User user) {
        return user.getActiveChallenges().stream()
            .filter(Challenge::isCompleted)
            .toList();
    }
}
