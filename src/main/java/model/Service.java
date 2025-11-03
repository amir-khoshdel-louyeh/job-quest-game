package model;

/**
 * Represents a purchasable service in the game, like buying food or visiting a doctor.
 * This is a data object that encapsulates the cost and effects of a service.
 */
public class Service {
    private final String name;
    private final String description;
    private final int cost;
    private final int healthEffect;
    private final int energyEffect;

    public Service(String name, String description, int cost, int healthEffect, int energyEffect) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.healthEffect = healthEffect;
        this.energyEffect = energyEffect;
    }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCost() { return cost; }
    public int getHealthEffect() { return healthEffect; }
    public int getEnergyEffect() { return energyEffect; }
}