package model;

public class Chef extends Identity {
    public Chef() { super("Chef"); }

    @Override
    public void performDailyWork(User user) {
        int pay = 250;
        user.setBalance(user.getBalance() + pay);
        user.setEnergy(user.getEnergy() - 25);
    }
}