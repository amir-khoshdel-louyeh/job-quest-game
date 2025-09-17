package controller;

import database.DatabaseUtil;
import model.Identity;
import model.IdentityFactory;
import model.User;

public class RegisterController {

    public boolean register(String username, String password, String identity, int balance) {
        // Create a complete User object on registration
        Identity identityObj = IdentityFactory.createIdentity(identity);
        User newUser = new User(username, password, identityObj, balance);
        // Pass the entire object to be saved
        return DatabaseUtil.createUser(newUser);
    }
}