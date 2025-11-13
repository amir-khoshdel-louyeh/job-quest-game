package model;

// This class stores money of the player.
// It shows Encapsulation: balance is private.
public class Wallet {
    // current money
    private int balance;
    // all money earned in life
    private int totalMoneyEarned;

    // Constructor: start with some money
    public Wallet(int startingBalance) {
        if (startingBalance < 0) {
            startingBalance = 0;
        }
        this.balance = startingBalance;
        this.totalMoneyEarned = 0;
    }

    // return balance
    public int getBalance() {
        return balance;
    }

    // set balance, never allow negative
    private void setBalance(int newBalance) {
        if (newBalance < 0) {
            newBalance = 0;
        }
        this.balance = newBalance;
    }

    // add money
    public void deposit(int amount) {
        if (amount > 0) {
            setBalance(balance + amount);
        }
    }

    // take money away
    public void withdraw(int amount) {
        if (amount > 0) {
            setBalance(balance - amount);
        }
    }

    // return total earned
    public int getTotalMoneyEarned() {
        return totalMoneyEarned;
    }

    // set total earned
    public void setTotalMoneyEarned(int total) {
        if (total < 0) {
            total = 0;
        }
        this.totalMoneyEarned = total;
    }

    // add to total earned
    public void addToTotalMoneyEarned(int amount) {
        if (amount > 0) {
            totalMoneyEarned = totalMoneyEarned + amount;
        }
    }
}
