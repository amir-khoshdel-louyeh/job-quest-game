package model;

// Base class for all items in shop/inventory.
// Shows Inheritance + Polymorphism: Food and Tool extend this class.
// Shows Abstraction: apply() is abstract, each child implements it differently.
public abstract class Item implements Purchasable {
    private String name;
    private String description;
    private int price;

    // Constructor
    public Item(String name, String description, int price) {
        // simple checks for beginner
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Item name must not be blank");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Item price must be >= 0");
        }
        this.name = name;
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
        this.price = price;
    }

    // return item name
    public String getName() {
        return name;
    }
    // return description
    public String getDescription() {
        return description;
    }
    // return price
    @Override
    public int getPrice() {
        return price;
    }

    // Each item applies its effect in its own way (Polymorphism)
    @Override
    public abstract void apply(User user);

    // Each item describes its effect
    @Override
    public abstract String getEffectDescription();

    // Is item removed after use? Food = true, Tool = false.
    // Child classes can override this.
    public boolean isConsumable() {
        return true;
    }
}
