package model.player;

// Money operations an upkeep rule may use.
public interface WalletTarget {
    int getBalance();

    boolean withdraw(int amount);
}
