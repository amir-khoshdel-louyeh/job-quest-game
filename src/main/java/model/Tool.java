package model;

/**
 * Model representing a tool that can be used during jobs or tasks.
 *
 * Follows Single Responsibility Principle by holding tool data only.
 */

public class Tool extends Item {
    public Tool(String name, String description, int price) {
        // construct a Tool item
        super(name, description, price);
    }
}
