package model.goals;

// Maintain-health challenge: fixed type, polymorphic copy.
public class MaintainHealthChallenge extends Challenge {
    public MaintainHealthChallenge(String id, String name, String description,
                                   int targetValue, int rewardMoney, int rewardExperience) {
        super(id, name, description, ChallengeType.MAINTAIN_HEALTH, targetValue, rewardMoney, rewardExperience);
    }

    @Override
    public Challenge copy() {
        return new MaintainHealthChallenge(getId(), getName(), getDescription(),
                getTargetValue(), getRewardMoney(), getRewardExperience());
    }
}
