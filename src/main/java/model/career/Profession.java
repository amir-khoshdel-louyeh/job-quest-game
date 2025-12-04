package model.career;

// Player profession contract.
// Implemented by ProfessionDefinition; used by Player, Job, and providers.
public interface Profession {
    
    String getProfessionName();
    String getDisplayName();
    String getDescription();
    int getStartingBalance();
    boolean isSameProfession(Profession other);
}
