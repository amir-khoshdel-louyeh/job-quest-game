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
    
    public static ChallengeService getInstance() {
        if (instance == null) {
            instance = new ChallengeService();
        }
        return instance;
    }
    
    /**
     * Initialize daily and main challenges for a new user.
     */
    public void initializeChallenges(User user) {
        if (user.getActiveChallenges().isEmpty()) {
            // Add main story challenges
            user.getActiveChallenges().addAll(provider.ChallengeProvider.getMainChallenges());
            
            // Add daily challenges
            refreshDailyChallenges(user);
        }
    }
    
    /**
     * Refresh daily challenges (should be called once per day).
     */
    public void refreshDailyChallenges(User user) {
        // Remove completed daily challenges
        user.getActiveChallenges().removeIf(q -> 
            q.getId().startsWith("daily_") && q.isCompleted()
        );
        
        // Add new daily challenges if not already active
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
    
    /**
     * Update challenge progress based on an action.
     */
    public List<Challenge> updateChallengeProgress(User user, Challenge.ChallengeType type, int amount) {
        List<Challenge> completedChallenges = new ArrayList<>();
        
        for (Challenge challenge : user.getActiveChallenges()) {
            if (challenge.getType() == type && !challenge.isCompleted()) {
                challenge.addProgress(amount);
                
                // Check if challenge was just completed
                if (challenge.isCompleted()) {
                    grantChallengeReward(user, challenge);
                    completedChallenges.add(challenge);
                }
            }
        }
        
        return completedChallenges;
    }
    
    /**
     * Grant rewards for completing a challenge.
     */
    private void grantChallengeReward(User user, Challenge challenge) {
        user.setBalance(user.getBalance() + challenge.getRewardMoney());
        user.addExperience(challenge.getRewardExperience());
        user.addReputation(2); // Challenges give reputation
    }
    
    /**
     * Get active challenges that are not yet completed.
     */
    public List<Challenge> getActiveIncompleteChallenges(User user) {
        return user.getActiveChallenges().stream()
            .filter(q -> !q.isCompleted())
            .toList();
    }
    
    /**
     * Get completed challenges.
     */
    public List<Challenge> getCompletedChallenges(User user) {
        return user.getActiveChallenges().stream()
            .filter(Challenge::isCompleted)
            .toList();
    }
}
