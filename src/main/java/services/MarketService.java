package services;

/**
 * Service managing marketplace operations such as item listings, prices and transactions.
 *
 * Follows Single Responsibility Principle by handling market logic only.
 */

import model.User;

public class MarketService {
    // execute a simple buyer->seller transfer if buyer has funds
    public static void buyItem(User buyer, User seller, int price) {
        if (buyer.getBalance() >= price) {
            buyer.withdraw(price);
            seller.deposit(price);
        } else {
            System.out.println("Not enough money!");
        }
    }
}
