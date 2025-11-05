package controller;

/**
 * Controller responsible for user registration and related validation.
 *
 * Follows Single Responsibility Principle by keeping registration flows isolated.
 */

import database.DatabaseUtil;
import model.Identity;
import provider.IdentityProvider;
import model.User;

public class RegisterController {

    public boolean register(String username, String password, String identity, int balance) {
        // register a new user with chosen identity and initial balance
    Identity identityObj = IdentityProvider.createIdentity(identity);
        User newUser = new User(username, password, identityObj, balance);
        // Step-4: first login day starts streak at 1 so JobService streak
        // bonus and Step-2 challenges have a sane baseline from day one.
        newUser.setCurrentStreak(1);
        newUser.setLastLoginDate(System.currentTimeMillis());
        // Pass the entire object to be saved
        return DatabaseUtil.createUser(newUser);
    }
}