package controller;

/**
 * Controller handling user authentication and login workflows.
 *
 * Follows Single Responsibility Principle by managing only login-related concerns.
 */

import model.User;
import database.DatabaseUtil;
import java.util.Objects;

public class LoginController {
    // authenticate user by username and password
    public User login(String username, String password) {
        User user = DatabaseUtil.getUser(username);
        // Use Objects.equals to avoid potential NPE and make the intent clear.
        if (user != null && Objects.equals(user.getPassword(), password)) {
            updateLoginStreak(user);
            DatabaseUtil.updateUser(user);
            return user;
        }
        return null;
    }

    /**
     * Step-4: daily streak owns to login (SRP). Same calendar day → keep,
     * next day → +1, gap ≥2 days → reset to 1. setCurrentStreak also tracks max.
     * Encapsulated in User; persisted by Repository.
     */
    private void updateLoginStreak(User user) {
        long now = System.currentTimeMillis();
        long last = user.getLastLoginDate();
        if (last <= 0) {
            user.setCurrentStreak(1);
        } else {
            java.time.LocalDate lastDay = java.time.Instant.ofEpochMilli(last)
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            java.time.LocalDate today = java.time.Instant.ofEpochMilli(now)
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            long gap = java.time.temporal.ChronoUnit.DAYS.between(lastDay, today);
            if (gap <= 0) {
                // same day: keep streak
            } else if (gap == 1) {
                user.setCurrentStreak(user.getCurrentStreak() + 1);
            } else {
                user.setCurrentStreak(1);
            }
        }
        user.setLastLoginDate(now);
    }
}
