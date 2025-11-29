package utils;

import java.util.Locale;

// Shop/service effect text.
public final class EffectText {
    private EffectText() {
    }

    public static String formatServiceEffect(int energyEffect, int healthEffect) {
        if (energyEffect == 0 && healthEffect == 0) {
            return "No effect";
        }
        String result = "";
        if (energyEffect > 0) {
            result = result + "+" + formatAmount(energyEffect) + " Energy";
        }
        if (healthEffect > 0) {
            if (!result.equals("")) {
                result = result + ", ";
            }
            result = result + "+" + formatAmount(healthEffect) + " Health";
        }
        return result;
    }

    private static String formatAmount(int amount) {
        return String.format(Locale.US, "%,d", amount);
    }
}
