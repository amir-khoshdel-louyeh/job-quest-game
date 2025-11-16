package model;

// A random event that changes money, health, or energy.
public class GameEvent {
    private String id;
    private String title;
    private String description;
    private EventType type;
    private int moneyEffect;
    private int healthEffect;
    private int energyEffect;
    private double probability;

    public enum EventType {
        POSITIVE,
        NEGATIVE,
        NEUTRAL
    }

    public GameEvent(String id, String title, String description, EventType type,
                    int moneyEffect, int healthEffect, int energyEffect, double probability) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.moneyEffect = moneyEffect;
        this.healthEffect = healthEffect;
        this.energyEffect = energyEffect;
        this.probability = probability;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public EventType getType() {
        return type;
    }
    public int getMoneyEffect() {
        return moneyEffect;
    }
    public int getHealthEffect() {
        return healthEffect;
    }
    public int getEnergyEffect() {
        return energyEffect;
    }
    public double getProbability() {
        return probability;
    }
}
