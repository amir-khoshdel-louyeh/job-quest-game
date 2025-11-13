package model;

// One profession choice shown on the start screen.
public class IdentityOption {
    private String name;
    private String description;
    private int price;
    private String imagePath;

    public IdentityOption(String name, String description, int price, String imagePath) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public String getImagePath() { return imagePath; }
}
