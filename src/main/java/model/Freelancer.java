package model;

public class Freelancer extends Identity {
    public Freelancer() { super("Freelancer"); }

    @Override
    public void performDailyWork(User user) {
        int pay = 200;
        user.setBalance(user.getBalance() + pay);
        user.setEnergy(user.getEnergy() - 20);
    }
}