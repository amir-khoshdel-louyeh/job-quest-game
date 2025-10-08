package model;

/**
 * Represents a random event that can occur during gameplay.
 */
public class GameEvent {
    private final String id;
    private final String title;
    private final String description;
    private final EventType type;
    private final int moneyEffect;
    private final int healthEffect;
    private final int energyEffect;
    private final double probability; // 0.0 to 1.0
    
    public enum EventType {
        POSITIVE,   // Good events
        NEGATIVE,   // Bad events
        NEUTRAL     // Neutral/informative events
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
    
    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public EventType getType() { return type; }
    public int getMoneyEffect() { return moneyEffect; }
    public int getHealthEffect() { return healthEffect; }
    public int getEnergyEffect() { return energyEffect; }
    public double getProbability() { return probability; }
    
    public String getIcon() {
        return switch (type) {
            case POSITIVE -> "✨";
            case NEGATIVE -> "⚠️";
            case NEUTRAL -> "ℹ️";
        };
    }
    
    public String getFormattedEffects() {
        StringBuilder sb = new StringBuilder();
        if (moneyEffect != 0) {
            sb.append(moneyEffect > 0 ? "+" : "").append("$").append(moneyEffect).append(" ");
        }
        if (healthEffect != 0) {
            sb.append(healthEffect > 0 ? "+" : "").append(healthEffect).append(" Health ");
        }
        if (energyEffect != 0) {
            sb.append(energyEffect > 0 ? "+" : "").append(energyEffect).append(" Energy");
        }
        return sb.toString().trim();
    }
}
