package services;

import java.util.List;

import model.goals.Challenge;
import model.goals.ChallengeType;
import model.player.PlayerProgress;

// The challenge operations controllers and queries need.
// Callers depend on this contract, not on ChallengeService.
public interface ChallengeRules {

    void initializeChallenges(PlayerProgress player);

    List<Challenge> updateChallengeProgress(PlayerProgress player,
                                            ChallengeType type, int amount);

    List<Challenge> getActiveIncompleteChallenges(PlayerProgress player);

    List<Challenge> getCompletedChallenges(PlayerProgress player);
}
