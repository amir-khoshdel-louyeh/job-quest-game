package utils;

// Single home for shop/service effect text. Models call this
// instead of each building "+X Energy" strings themselves.
public final class EffectText {
    private EffectText() {
    }

    public static String formatServiceEffect(int energyEffect, int healthEffect) {
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
