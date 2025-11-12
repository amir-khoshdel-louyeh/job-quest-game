package model;

/**
 * OOP Step 3: common abstraction for anything buyable with an effect.
 *
 * <p>Previously {@link Item} (storable, goes to inventory) and {@link Service}
 * (instant, applied on purchase) duplicated the same fields
 * ({@code name/description/price/energyEffect/healthEffect}) with no shared
 * type. Controllers and shop UIs therefore needed two parallel code paths.
 * Now both implement this interface, so shop code depends on the abstraction
 * (Dependency Inversion) and effects apply polymorphically via
 * {@link #apply(User)}.</p>
 */
public interface Purchasable {
    String getName();
    String getDescription();
    int getPrice();
    void apply(User user);
    String getEffectDescription();

    /**
     * Shared effect formatting (DRY via interface default method):
     * both {@link ConsumableItem} and {@link Service} use this.
     */
    default String formatEffect(int energyEffect, int healthEffect) {
        if (energyEffect == 0 && healthEffect == 0) return "No effect";
        StringBuilder sb = new StringBuilder();
        if (energyEffect > 0) sb.append("+").append(String.format("%,d", energyEffect)).append(" Energy");
        if (healthEffect > 0) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("+").append(healthEffect).append(" Health");
        }
        return sb.toString();
    }
}
