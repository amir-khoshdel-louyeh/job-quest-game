package model;

/**
 * OOP Step 6: money handling extracted from the {@link User} god-object.
 *
 * <p>Single Responsibility: balances and lifetime earnings only. Clamps at
 * zero so callers never deal with negative money. Independently testable
 * without constructing a full User.</p>
 */
public class Wallet {
    private int balance;
    private int totalMoneyEarned;

    public Wallet(int startingBalance) {
        this.balance = Math.max(0, startingBalance);
        this.totalMoneyEarned = 0;
    }

    public int getBalance() { return balance; }

    private void setBalance(int balance) { this.balance = Math.max(0, balance); }

    public void deposit(int amount) {
        if (amount > 0) setBalance(this.balance + amount);
    }

    public void withdraw(int amount) {
        if (amount > 0) setBalance(this.balance - amount);
    }

    public int getTotalMoneyEarned() { return totalMoneyEarned; }

    public void setTotalMoneyEarned(int total) {
        this.totalMoneyEarned = Math.max(0, total);
    }

    public void addToTotalMoneyEarned(int amount) {
        if (amount > 0) this.totalMoneyEarned += amount;
    }
}
