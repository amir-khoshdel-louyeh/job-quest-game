package model.goals;

// Complete-jobs challenge: fixed type, polymorphic copy.
public class CompleteJobsChallenge extends Challenge {
    public CompleteJobsChallenge(String id, String name, String description,
                                 int targetValue, int rewardMoney, int rewardExperience) {
        super(id, name, description, ChallengeType.COMPLETE_JOBS, targetValue, rewardMoney, rewardExperience);
    }

    @Override
    public Challenge copy() {
        return new CompleteJobsChallenge(getId(), getName(), getDescription(),
                getTargetValue(), getRewardMoney(), getRewardExperience());
    }
}
