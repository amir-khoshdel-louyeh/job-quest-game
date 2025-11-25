package model.player;

import java.util.Objects;

// The User Wallet.
// Used by model.player.User (sole owner); read via User/UserController/EconomyController.

public class Wallet {
    private int balance;
    private int totalMoneyEarned;

    // ----- Construction -----
    public Wallet(int startingBalance) {
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
    public void deposit(int amount) {
        if (amount > 0) {
            setBalance(balance + amount);
        }
    }

    public void withdraw(int amount) {
        if (amount > 0) {
            setBalance(balance - amount);
        }
    }

    public void earn(int amount) {
        if (amount > 0) {
            setBalance(balance + amount);
            totalMoneyEarned = totalMoneyEarned + amount;
        }
    }

    // ----- Demo scenario -----
    public void debugSetBalance(int newBalance) {
        setBalance(newBalance);
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
