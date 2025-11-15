package model;

// The identity of a player contract.
public interface Identity {
    
    String getProfessionName();
    String getDisplayName();
    String getDescription();
    int getStartingBalance();
    String getImagePath();
    String getWorkGameKind();
    boolean isSameProfession(Identity other);
}
