package controller;

/**
 * Controller responsible for saving and loading game state (persistence operations).
 *
 * Follows Single Responsibility Principle by encapsulating save/load logic only.
 */

import database.DatabaseUtil;
import model.User;

public class SaveController {
    public boolean saveGame(User user, long sessionStartTime) {
        // persist user save data and update total play time
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        long newTotalPlayTime = user.getTotalPlayTime() + sessionDuration;
        user.setTotalPlayTime(newTotalPlayTime);
        if (DatabaseUtil.updateUser(user)) {
            return true;
        }
        // Row missing (e.g. Quick Demo user never registered) → insert instead.
        // If the DB itself is down, both attempts fail and we report failure.
        return DatabaseUtil.createUser(user);
    }
}
