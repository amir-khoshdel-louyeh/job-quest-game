package model.achievement;

import model.Achievement;
import model.User;

// Rewards holding a lot of money at once.
public class RichPlayerAchievement extends Achievement {
    public RichPlayerAchievement(String id, String name, String description, int rewardMoney, int requiredValue) {
        super(id, name, description, rewardMoney, requiredValue);
    }

    @Override
    protected int getCurrentValue(User user) {
        return user.getBalance();
    }
}
