package model.career;

import java.util.Objects;


// One profession with its starting balance.
// Implements Profession; read via ProfessionRegistry.
public final class ProfessionDefinition implements Profession {
    private final String professionName;
    private final String displayName;
    private final String description;
    private final int startingBalance;

    // ----- Construction -----
    public ProfessionDefinition(String professionName, String displayName, String description,
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
    public boolean isSameProfession(Profession other) {
        if (other == null) {
            return false;
        }
        return professionName.equals(other.getProfessionName());
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
        ProfessionDefinition other = (ProfessionDefinition) obj;
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
