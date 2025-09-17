package controller;

import model.User;

public class UserController {
    private User user;

    public UserController(User user) {
        this.user = user;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public int getBalance() {
        return user.getBalance();
    }

    public void addBalance(int amount) {
        user.setBalance(user.getBalance() + amount);
    }

    public boolean deductBalance(int amount) {
        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            return true;
        }
        return false;
    }

    public int getHealth() {
        return user.getHealth();
    }

    public void increaseHealth(int amount) {
        user.setHealth(Math.min(100, user.getHealth() + amount));
    }

    public void decreaseHealth(int amount) {
        user.setHealth(Math.max(0, user.getHealth() - amount));
    }

    public int getEnergy() {
        return user.getEnergy();
    }

    public void increaseEnergy(int amount) {
        user.setEnergy(Math.min(100_000, user.getEnergy() + amount));
    }

    public void decreaseEnergy(int amount) {
        user.setEnergy(Math.max(0, user.getEnergy() - amount));
    }

    public boolean isBlocked() {
        return user.isBlocked();
    }

    public void blockUser(long durationMillis) {
        user.setBlockedUntil(System.currentTimeMillis() + durationMillis);
    }

    public void unblockUser() {
        user.setBlockedUntil(0);
    }

    public long getBlockedUntil() {
        return user.getBlockedUntil();
    }

    public User getUser() {
        return user;
    }
}
