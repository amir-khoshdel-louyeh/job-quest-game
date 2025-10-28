package controller;

import database.DatabaseUtil;
import model.User;

public class SaveController {
    public boolean saveGame(User user, long sessionStartTime) {
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        long newTotalPlayTime = user.getTotalPlayTime() + sessionDuration;
        user.setTotalPlayTime(newTotalPlayTime);
        return DatabaseUtil.updateUser(user);
    }
}
