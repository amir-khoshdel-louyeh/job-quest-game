package model;

/**
 * Represents a single task that a user can perform.
 * This is a data object that encapsulates all properties of a task.
 */
public class Task {
    private final String name;
    private final int payment;
    private final int energyCost;

    public Task(String name, int payment, int energyCost) {
        this.name = name;
        this.payment = payment;
        this.energyCost = energyCost;
    }
    public String getName() { return name; }
    public int getPayment() { return payment; }
    public int getEnergyCost() { return energyCost; }
}