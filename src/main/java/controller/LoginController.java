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
            return user;
        }
        return null;
    }
}
