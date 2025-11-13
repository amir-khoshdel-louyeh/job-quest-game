package model.identity;

import model.Identity;

// The doctor profession.
public class Doctor implements Identity {
    @Override
    public String getProfessionName() { return "Doctor"; }
}
