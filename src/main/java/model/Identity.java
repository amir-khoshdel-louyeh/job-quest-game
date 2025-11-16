package model;

// Shared kernel: kept in root, used by player/career/providers.
public interface Identity {
    
    String getProfessionName();
    String getDisplayName();
    String getDescription();
    int getStartingBalance();
    String getImagePath();
    String getWorkGameKind();
    boolean isSameProfession(Identity other);
}
