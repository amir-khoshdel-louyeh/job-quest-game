package model.goals;

// Learn-skills challenge: fixed type, polymorphic copy.
public class LearnSkillsChallenge extends Challenge {
    public LearnSkillsChallenge(String id, String name, String description,
                                int targetValue, int rewardMoney, int rewardExperience) {
        super(id, name, description, ChallengeType.LEARN_SKILLS, targetValue, rewardMoney, rewardExperience);
    }

    @Override
    public Challenge copy() {
        return new LearnSkillsChallenge(getId(), getName(), getDescription(),
                getTargetValue(), getRewardMoney(), getRewardExperience());
    }
}
