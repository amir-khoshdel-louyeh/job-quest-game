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
        String cleaned = identityName.trim();
        if (cleaned.equals(Doctor.PROFESSION_NAME)) {
            return new Doctor();
        } else if (cleaned.equals(Programmer.PROFESSION_NAME)) {
            return new Programmer();
        } else if (cleaned.equals(LogoDesigner.PROFESSION_NAME)
                || cleaned.equals(LogoDesigner.DISPLAY_NAME)) {
            // DISPLAY_NAME accepted only for old save compatibility.
            return new LogoDesigner();
        } else if (cleaned.equals(Typist.PROFESSION_NAME)) {
            return new Typist();
        } else {
            throw new IllegalArgumentException("Unknown identity type: " + identityName);
        }
    }
}
