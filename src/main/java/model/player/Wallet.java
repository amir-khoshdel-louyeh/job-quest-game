package model.player;

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

    // Exact setter. Values below zero become zero.
    // Lets callers say "set to X" and trust the domain to clamp (Tell-Don't-Ask).
    public void setBalance(int newBalance) {
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

    public void addToTotalMoneyEarned(int amount) {
        if (amount > 0) {
            totalMoneyEarned = totalMoneyEarned + amount;
        }
    }
}
