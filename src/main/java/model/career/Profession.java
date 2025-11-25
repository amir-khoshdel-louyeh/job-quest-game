package model.career;

import java.util.Objects;

import model.Identity;

// Single data-driven profession. Replaces one-class-per-job inheritance:
// professions differ only in data, not behavior.
public final class Profession implements Identity {
    private final String professionName;
    private final String displayName;
    private final String description;
    private final int startingBalance;

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

    @Override
    public String getProfessionName() { return professionName; }

    @Override
    public String getDisplayName() { return displayName; }

    @Override
    public String getDescription() { return description; }

    @Override
    public int getStartingBalance() { return startingBalance; }

    @Override
    public boolean isSameProfession(Identity other) {
        if (other == null) {
            return false;
        }
        // Single name rule lives in equals: a profession IS its name.
        return equals(other);
    }

    // Value equality by profession name (the profession's identity).
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
