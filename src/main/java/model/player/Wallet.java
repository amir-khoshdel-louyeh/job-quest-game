package model.player;

import java.util.Objects;

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
    // Private: only deposit/withdraw/earn/debug may set an exact value
    // so game logic cannot forge money (Tell-Don't-Ask).
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

    // Atomic earning: balance + lifetime total move together.
    // Use this for job income so callers cannot forget the second half.
    // Pure gifts/bonuses (events, achievements) keep using deposit().
    public void earn(int amount) {
        if (amount > 0) {
            setBalance(balance + amount);
            totalMoneyEarned = totalMoneyEarned + amount;
        }
    }

    // Demo/debug only: lets the Developer panel set an exact value.
    // Game logic must use deposit/withdraw/earn instead.
    public void debugSetBalance(int newBalance) {
        setBalance(newBalance);
    }

    public int getTotalMoneyEarned() {
        return totalMoneyEarned;
    }

    // Value equality: two wallets match when money matches.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Wallet other = (Wallet) obj;
        return balance == other.balance
                && totalMoneyEarned == other.totalMoneyEarned;
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
