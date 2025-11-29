package model.player;

// Health and energy as the bars render them.
public interface VitalsView {
    int getHealth();

    int getHealthPercentage();

    int getEnergy();

    int getEnergyPercentage();
}
