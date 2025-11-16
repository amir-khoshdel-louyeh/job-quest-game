package model.store;

// A random event that changes money, health, or energy.
// Immutable value object: all fields final, validated in constructor.
public final class GameEvent {
    private final String id;
    private final String title;
    private final String description;
    private final EventType type;
    private final int moneyEffect;
    private final int healthEffect;
    private final int energyEffect;
    private final double probability;

    public enum EventType {
        POSITIVE,
        NEGATIVE,
        NEUTRAL
    }

    public GameEvent(String id, String title, String description, EventType type,
                    int moneyEffect, int healthEffect, int energyEffect, double probability) {
        if (id == null || id.trim().equals("")) {
            throw new IllegalArgumentException("id required");
        }
        if (type == null) {
            throw new IllegalArgumentException("type required");
        }
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException("probability must be 0..1");
        }
        this.id = id;
        if (title == null) {
            this.title = "";
        } else {
            this.title = title;
        }
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
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
