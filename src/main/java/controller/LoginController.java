package controller;

import model.User;
import database.DatabaseUtil;

// This class handles login.
// It checks username and password from database.
public class LoginController {

    // Try to login, return User if ok, null if failed.
    public User login(String username, String password) {
        User user = DatabaseUtil.getUser(username);
        if (user != null) {
            String realPass = user.getPassword();
            boolean ok = false;
            if (realPass == null && password == null) {
                ok = true;
            } else if (realPass != null && realPass.equals(password)) {
                ok = true;
            }
            if (ok) {
                updateLoginStreak(user);
                DatabaseUtil.updateUser(user);
                return user;
            }
        }
        return null;
    }

    // Update daily streak.
    // Same day -> keep, next day -> +1, gap 2+ days -> reset to 1.
    // Uses simple millis math, no java.time (beginner way).
    private void updateLoginStreak(User user) {
        long now = System.currentTimeMillis();
        long last = user.getLastLoginDate();
        if (last <= 0) {
            user.setCurrentStreak(1);
        } else {
            long oneDay = 24 * 60 * 60 * 1000L;
            long diff = now - last;
            long days = diff / oneDay;
            if (days <= 0) {
                // same day, do nothing
            } else if (days == 1) {
                user.setCurrentStreak(user.getCurrentStreak() + 1);
            } else {
                user.setCurrentStreak(1);
            }
        }
        user.setLastLoginDate(now);
    }
}
