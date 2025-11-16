package model.goals;

// Earn-money challenge: fixed type, polymorphic copy.
public class EarnMoneyChallenge extends Challenge {
    public EarnMoneyChallenge(String id, String name, String description,
                              int targetValue, int rewardMoney, int rewardExperience) {
        super(id, name, description, ChallengeType.EARN_MONEY, targetValue, rewardMoney, rewardExperience);
    }

    @Override
    public Challenge copy() {
        return new EarnMoneyChallenge(getId(), getName(), getDescription(),
                getTargetValue(), getRewardMoney(), getRewardExperience());
    }
}
