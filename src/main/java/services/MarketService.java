package services;

import model.User;

public class MarketService {
    public static void buyItem(User buyer, User seller, int price) {
        if (buyer.getBalance() >= price) {
            buyer.withdraw(price);
            seller.deposit(price);
        } else {
            System.out.println("Not enough money!");
        }
    }
}
