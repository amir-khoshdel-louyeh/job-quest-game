package model;

public class Doctor extends Identity {
    public Doctor() { super("Doctor"); }

    @Override
    public void performDailyWork(User user) {
        int pay = 300;
        user.setBalance(user.getBalance() + pay);
        user.setEnergy(user.getEnergy() - 30);
    }
}