package model.career;

import java.util.Objects;

import model.Identity;

// One profession with its starting balance.
// Implements Identity; read via IdentityProvider.
public final class Profession implements Identity {
    private final String professionName;
    private final String displayName;
    private final String description;
    private final int startingBalance;

    // ----- Construction -----
    public Profession(String professionName, String displayName, String description,
                      int startingBalance) {
        if (professionName == null || professionName.trim().equals("")) {
            throw new IllegalArgumentException("professionName required");
        }
        this.professionName = professionName;
        this.displayName = displayName;
        this.description = description;
        this.startingBalance = startingBalance;
    }

    // ----- Getter -----
    @Override
    public String getProfessionName() { return professionName; }

    @Override
    public String getDisplayName() { return displayName; }

    @Override
    public String getDescription() { return description; }

    @Override
    public int getStartingBalance() { return startingBalance; }

    // ----- Queries -----
    @Override
    public boolean isSameProfession(Identity other) {
        if (other == null) {
            return false;
        }
        return equals(other);
    }

    // Equal by profession name.
    // ----- Object overrides -----
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Profession other = (Profession) obj;
        return Objects.equals(professionName, other.professionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(professionName);
    }

    @Override
    public String toString() {
        return displayName + " [" + professionName + "]";
    }
}
