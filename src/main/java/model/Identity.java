package model;

// Player profession contract.
// Implemented by Profession; used by User, Job, and providers.
public interface Identity {
    
    String getProfessionName();
    String getDisplayName();
    String getDescription();
    int getStartingBalance();
    boolean isSameProfession(Identity other);
}
