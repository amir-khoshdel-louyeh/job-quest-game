package model.store;

// Something the player can buy in the shop.
// Applied to a ServiceTarget; implemented by Service.
public interface Purchasable {
    String getName();
    String getDescription();
    int getPrice();
    void apply(ServiceTarget target);
}
