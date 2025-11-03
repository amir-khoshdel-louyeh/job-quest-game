package model;

public class IdentityOption {
    /**
     * Represents an option for identities a player can choose (e.g., professions or roles).
     *
     * Follows Single Responsibility Principle by modelling identity option data only.
     */

    private String name;
    private String description;
    private int price;
    private String imagePath;

    public IdentityOption(String name, String description, int price, String imagePath) {
        // construct an identity option for selection
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
    }

    // return option name
    public String getName() { return name; }
    // return option description
    public String getDescription() { return description; }
    // return option price
    public int getPrice() { return price; }
    // return image path
    public String getImagePath() { return imagePath; }
}
