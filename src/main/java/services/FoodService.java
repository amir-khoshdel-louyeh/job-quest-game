
package services;

import model.User;

/**
 * Service for handling food-related operations.
 * Follows Single Responsibility Principle and uses Singleton pattern.
 */
public class FoodService {
    private static FoodService instance;

    public static FoodService getInstance() {
        if (instance == null) {
            instance = new FoodService();
        }
        return instance;
    }

    // purchase and consume food for buyer, optionally paying a seller
    public boolean processFoodPurchase(User buyer, int hungerRestore, int cost, User seller) {
        if (buyer == null) {
            throw new IllegalArgumentException("Buyer cannot be null");
        }
        if (buyer.getBalance() < cost) {
            System.out.println("Not enough money to buy food!");
            return false;
        }
        buyer.withdraw(cost);
        if (seller != null) {
            seller.deposit(cost);
        }
        buyer.gainHealth(hungerRestore);
        return true;
    }

    // Static method for backward compatibility
    // backward-compatible static helper to eat food
    public static void eatFood(User user, int hungerRestore, int cost, User seller) {
        getInstance().processFoodPurchase(user, hungerRestore, cost, seller);
    }
}


