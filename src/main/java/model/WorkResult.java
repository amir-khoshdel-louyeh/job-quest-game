package model;

/**
 * Represents the outcome of a work action. This is a Data Transfer Object (DTO)
 * used to pass results from an Identity's strategy to the controller.
 */
public class WorkResult {

    public enum Type {
        INSTANT_EARNING,
        REQUIRES_DIALOG
    }

    private final int moneyEarned;
    private final int energyCost;
    private final Type type;

    public WorkResult(int moneyEarned, int energyCost, Type type) {
        this.moneyEarned = moneyEarned;
        this.energyCost = energyCost;
        this.type = type;
    }

    public int getMoneyEarned() { return moneyEarned; }
    public int getEnergyCost() { return energyCost; }
    public Type getType() { return type; }
}