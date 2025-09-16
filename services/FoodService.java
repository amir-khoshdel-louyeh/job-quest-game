package services;

import model.User;

public class FoodService {
    public static void eatFood(User user, int hungerRestore, int cost, User seller) {
        if (user.getBalance() >= cost) {
            user.setBalance(user.getBalance() - cost);
            if (seller != null) seller.setBalance(seller.getBalance() + cost);
            user.setHealth(Math.min(100, user.getHealth() + hungerRestore));
        } else {
            System.out.println("Not enough money to buy food!");
        }
    }
}
