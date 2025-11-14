package provider;

import model.Identity;
import model.identity.Doctor;
import model.identity.LogoDesigner;
import model.identity.Programmer;
import model.identity.Typist;

// Builds the starting profession the player picks.
public class IdentityProvider {

    public static Identity createIdentity(String identityName) {
        if (identityName == null) {
            throw new IllegalArgumentException("Unknown identity type: null");
        }
        if (identityName.equals("Doctor")) {
            return new Doctor();
        } else if (identityName.equals("Programmer")) {
            return new Programmer();
        } else if (identityName.equals("LogoDesigner") || identityName.equals("Logo Designer")) {
            return new LogoDesigner();
        } else if (identityName.equals("Typist")) {
            return new Typist();
        } else {
            throw new IllegalArgumentException("Unknown identity type: " + identityName);
        }
    }
}
