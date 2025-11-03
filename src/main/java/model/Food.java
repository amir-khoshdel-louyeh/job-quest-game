package model;

/**
 * Model representing a food item that a player can consume to gain effects.
 *
 * Follows Single Responsibility Principle by representing food data only.
 */

public class Food extends Item {
    public Food(String name, String description, int price) {
        // construct a Food item
        super(name, description, price);
    }
}
