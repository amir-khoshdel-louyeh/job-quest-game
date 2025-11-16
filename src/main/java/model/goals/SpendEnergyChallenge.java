package model.goals;

// Spend-energy challenge: fixed type, polymorphic copy.
public class SpendEnergyChallenge extends Challenge {
    public SpendEnergyChallenge(String id, String name, String description,
                                int targetValue, int rewardMoney, int rewardExperience) {
        super(id, name, description, ChallengeType.SPEND_ENERGY, targetValue, rewardMoney, rewardExperience);
    }

    @Override
    public Challenge copy() {
        return new SpendEnergyChallenge(getId(), getName(), getDescription(),
                getTargetValue(), getRewardMoney(), getRewardExperience());
    }
}
