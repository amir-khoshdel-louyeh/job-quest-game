package model;

import provider.IdentityProvider;

// An option for a player to choose an identity.
public class IdentityOption {
    private Identity identity;

    public IdentityOption(Identity identity) {
        if (identity == null) {
            throw new IllegalArgumentException("identity is null");
        }
        this.identity = identity;
    }

    public IdentityOption(String name, String description, int price, String imagePath) {
        this.identity = IdentityProvider.createIdentity(name);
    }


    // Pretty name for the screen, example "Logo Designer".
    public String getName() { return identity.getDisplayName(); }

    // Stable id, example "LogoDesigner".
    public String getProfessionName() { return identity.getProfessionName(); }

    public String getDescription() { return identity.getDescription(); }

    public int getPrice() { return identity.getStartingBalance(); }

    public String getImagePath() { return identity.getImagePath(); }

    public Identity getIdentity() { return identity; }

    public Identity createIdentity() {
        return IdentityProvider.createIdentity(identity.getProfessionName());
    }
}
