package services;

import model.User;

/**
 * Service for handling food-related operations.
 * Follows Single Responsibility Principle and uses Singleton pattern.
 */
public class FoodService {
    private static FoodService instance;
    
    private FoodService() {
        // Private constructor for Singleton
    }
    
    public static FoodService getInstance() {
        if (instance == null) {
            instance = new FoodService();
        }
        return instance;
    }
    
    /**
     * Process food purchase and consumption.
     * @param buyer The user buying and eating the food
     * @param hungerRestore Amount of health to restore
     * @param cost Cost of the food
     * @param seller The seller (can be null for system sales)
     * @return true if purchase was successful, false otherwise
     */
    public boolean processFoodPurchase(User buyer, int hungerRestore, int cost, User seller) {
        if (buyer == null) {
            throw new IllegalArgumentException("Buyer cannot be null");
        }
        
        if (buyer.getBalance() < cost) {
            System.out.println("Not enough money to buy food!");
            return false;
        }
        
        buyer.setBalance(buyer.getBalance() - cost);
        
        if (seller != null) {
            seller.setBalance(seller.getBalance() + cost);
        }
        
        buyer.setHealth(Math.min(100, buyer.getHealth() + hungerRestore));
        return true;
    }
    
    // Static method for backward compatibility
    @Deprecated
    public static void eatFood(User user, int hungerRestore, int cost, User seller) {
        getInstance().processFoodPurchase(user, hungerRestore, cost, seller);
    }
}


