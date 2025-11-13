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

    // Picks a small icon based on whether the event is good, bad, or neutral.
    public String getIcon() {
        if (type == EventType.POSITIVE) {
            return "✨";
        } else if (type == EventType.NEGATIVE) {
            return "⚠️";
        } else {
            return "ℹ️";
        }
    }

    // Builds text like "+$100 +10 Health" from the stored effects.
    public String getFormattedEffects() {
        String result = "";
        if (moneyEffect != 0) {
            if (moneyEffect > 0) {
                result = result + "+$" + moneyEffect + " ";
            } else {
                result = result + "$" + moneyEffect + " ";
            }
        }
        if (healthEffect != 0) {
            if (healthEffect > 0) {
                result = result + "+" + healthEffect + " Health ";
            } else {
                result = result + healthEffect + " Health ";
            }
        }
        if (energyEffect != 0) {
            if (energyEffect > 0) {
                result = result + "+" + energyEffect + " Energy";
            } else {
                result = result + energyEffect + " Energy";
            }
        }
        return result.trim();
    }
}
