package model.player;

import java.util.Objects;

// Tracks a user's money balance.
// Used by model.player.User (sole owner); read via User/UserController/EconomyController.

public final class Wallet {
    private int balance;
    private int totalMoneyEarned;

    // ----- Construction -----
    Wallet(int startingBalance) {
        if (startingBalance < 0) {
            startingBalance = 0;
        }
        this.balance = startingBalance;
        this.totalMoneyEarned = 0;
    }

    // ----- Setter -----
    private void setBalance(int newBalance) {
        if (newBalance < 0) {
            newBalance = 0;
        }
        this.balance = newBalance;
    }


    // ----- Getter -----
    public int getBalance() {
        return balance;
    }

    public int getTotalMoneyEarned() {
        return totalMoneyEarned;
    }

    // ----- Commands -----
    public boolean deposit(int amount) {
        if (amount > 0) {
            setBalance(balance + amount);
            return true;
        }
        return false;
    }

    public boolean withdraw(int amount) {
        if (amount > 0 && balance >= amount) {
            setBalance(balance - amount);
            return true;
        }
        return false;
    }

    public boolean earn(int amount) {
        if (deposit(amount)) {
            totalMoneyEarned = totalMoneyEarned + amount;
            return true;
        }
        return false;
    }

    // ----- Object overrides -----
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Wallet other = (Wallet) obj;
        return balance == other.balance && totalMoneyEarned == other.totalMoneyEarned;
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, totalMoneyEarned);
    }

    @Override
    public String toString() {
        return "Wallet [balance $" + balance + ", earned $" + totalMoneyEarned + "]";
    }
}
