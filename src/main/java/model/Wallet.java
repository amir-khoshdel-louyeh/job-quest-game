package model;

// Holds the player's money and guards against negative balance.
public class Wallet {
    private int balance;
    private int totalMoneyEarned;

    public Wallet(int startingBalance) {
        if (startingBalance < 0) {
            startingBalance = 0;
        }
        this.balance = startingBalance;
        this.totalMoneyEarned = 0;
    }

    public int getBalance() {
        return balance;
    }

    // Values below zero become zero.
    private void setBalance(int newBalance) {
        if (newBalance < 0) {
            newBalance = 0;
        }
        this.balance = newBalance;
    }

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

    public int getTotalMoneyEarned() {
        return totalMoneyEarned;
    }

    public void setTotalMoneyEarned(int total) {
        if (total < 0) {
            total = 0;
        }
        this.totalMoneyEarned = total;
    }

    public void addToTotalMoneyEarned(int amount) {
        if (amount > 0) {
            totalMoneyEarned = totalMoneyEarned + amount;
        }
    }
}
