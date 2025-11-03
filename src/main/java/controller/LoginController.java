package controller;

/**
 * Controller handling user authentication and login workflows.
 *
 * Follows Single Responsibility Principle by managing only login-related concerns.
 */

import model.User;
import database.DatabaseUtil;

public class LoginController {
    public User login(String username, String password) {
        // authenticate user by username and password
        User user = DatabaseUtil.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
