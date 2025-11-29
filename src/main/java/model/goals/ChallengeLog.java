package model.goals;

import java.util.List;

// The challenge operations a rule needs. Implemented by User and by
// UserController, so services never depend on the whole aggregate.
public interface ChallengeLog {
    List<Challenge> getActiveChallenges();

    List<Challenge> updateChallengeProgress(Challenge.ChallengeType type, int amount);

    void addChallenges(List<Challenge> challenges);

    boolean hasChallenge(String challengeId);
}
