package controller;

import database.DatabaseUtil;

public class RegisterController {

    // متد با آرگومان
    public boolean register(String username, String password, String identity, int balance) {
        return DatabaseUtil.saveUser(username, password, identity, balance);
    }
}