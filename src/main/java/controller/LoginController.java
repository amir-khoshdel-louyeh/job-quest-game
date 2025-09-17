package controller;

import model.User;
import database.DatabaseUtil;

public class LoginController {
    public User login(String username, String password) {
        User user = DatabaseUtil.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
