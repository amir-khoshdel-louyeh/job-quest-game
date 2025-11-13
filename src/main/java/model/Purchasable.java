package model;

// This interface represents anything that can be bought in the game.
// Both Item (stored in inventory) and Service (used instantly) implement it.
// This shows Interface + Polymorphism: shop can treat them the same way.
public interface Purchasable {
    String getName();
    String getDescription();
    int getPrice();
    // Apply effect on player
    void apply(User user);
    String getEffectDescription();

    // Helper method to make effect text like "+100 Energy, +10 Health".
    // Uses simple if and string concatenation, no advanced formatting.
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
