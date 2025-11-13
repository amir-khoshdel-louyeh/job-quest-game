package model;

// Something the player can buy in the shop.
public interface Purchasable {
    String getName();
    String getDescription();
    int getPrice();
    void apply(User user);
    String getEffectDescription();

    // Builds text like "+100 Energy, +10 Health".
    default String formatEffect(int energyEffect, int healthEffect) {
        if (energyEffect == 0 && healthEffect == 0) {
            return "No effect";
        }
        String result = "";
        if (energyEffect > 0) {
            result = result + "+" + energyEffect + " Energy";
        }
        if (healthEffect > 0) {
            if (!result.equals("")) {
                result = result + ", ";
            }
            result = result + "+" + healthEffect + " Health";
        }
        return result;
    }
}
