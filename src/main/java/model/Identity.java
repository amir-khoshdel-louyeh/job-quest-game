package model;

// Shared kernel: kept in model root because player (User),
// career (Job, LearnableSkill) and providers all depend on it.
public interface Identity {
    
    String getProfessionName();
    String getDisplayName();
    String getDescription();
    int getStartingBalance();
    boolean isSameProfession(Identity other);
}
