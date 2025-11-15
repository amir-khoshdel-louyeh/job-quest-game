package model.identity;

import model.Identity;

// Shared code for all professions.
// Subclasses only return their own data, they don't repeat the comparing logic.
public abstract class BaseIdentity implements Identity {

    @Override
    public boolean isSameProfession(Identity other) {
        if (other == null) {
            return false;
        }
        return getProfessionName().equals(other.getProfessionName());
    }
}
