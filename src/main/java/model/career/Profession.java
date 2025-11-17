package model.career;

import model.Identity;
import model.WorkGameKind;

// Single data-driven profession. Replaces one-class-per-job inheritance:
// professions differ only in data, not behavior.
public final class Profession implements Identity {
    private final String professionName;
    private final String displayName;
    private final String description;
    private final int startingBalance;
    private final String imagePath;
    private final WorkGameKind workGameKind;

    public Profession(String professionName, String displayName, String description,
                      int startingBalance, String imagePath, WorkGameKind workGameKind) {
        if (professionName == null || professionName.trim().equals("")) {
            throw new IllegalArgumentException("professionName required");
        }
        if (workGameKind == null) {
            throw new IllegalArgumentException("workGameKind required");
        }
        this.professionName = professionName;
        this.displayName = displayName;
        this.description = description;
        this.startingBalance = startingBalance;
        this.imagePath = imagePath;
        this.workGameKind = workGameKind;
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
    public String getImagePath() { return imagePath; }

    @Override
    public WorkGameKind getWorkGameKind() { return workGameKind; }

    @Override
    public boolean isSameProfession(Identity other) {
        if (other == null) {
            return false;
        }
        return professionName.equals(other.getProfessionName());
    }
}
