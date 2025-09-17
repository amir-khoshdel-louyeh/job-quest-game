package services;

import model.User;

public class MarketService {
    public static void buyItem(User buyer, User seller, int price) {
        if (buyer.getBalance() >= price) {
            buyer.setBalance(buyer.getBalance() - price);
            seller.setBalance(seller.getBalance() + price);
        } else {
            System.out.println("Not enough money!");
        }
    }
}
