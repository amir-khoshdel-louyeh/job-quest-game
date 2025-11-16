package provider;

import model.Identity;
import model.identity.Profession;

// Builds the starting profession the player picks.
// Data lives in one table here, not in subclasses.
public class IdentityProvider {

    public static Identity createIdentity(String identityName) {
        if (identityName == null) {
            throw new IllegalArgumentException("Unknown identity type: null");
        }
        String cleaned = identityName.trim();
        if (cleaned.equals("Doctor")) {
            return new Profession("Doctor", "Doctor", "Heal players and earn big money.",
                    500, "/images/doctor.png", "DIAGNOSIS");
        } else if (cleaned.equals("Programmer")) {
            return new Profession("Programmer", "Programmer", "Write code and solve complex problems.",
                    300, "/images/programmer.png", "CODE");
        } else if (cleaned.equals("LogoDesigner") || cleaned.equals("Logo Designer")) {
            // DISPLAY_NAME accepted only for old save compatibility.
            return new Profession("LogoDesigner", "Logo Designer", "Create logos for clients.",
                    200, "/images/logodesigner.png", "DESIGN");
        } else if (cleaned.equals("Typist")) {
            return new Profession("Typist", "Typist", "Type documents for clients.",
                    200, "/images/typist.png", "TYPING");
        } else {
            throw new IllegalArgumentException("Unknown identity type: " + identityName);
        }
    }
}
