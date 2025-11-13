package provider;

import model.Identity;
import model.identity.Doctor;
import model.identity.LogoDesigner;
import model.identity.Programmer;
import model.identity.Typist;

// This class creates Identity objects from a name.
// It shows Polymorphism: return type is Identity interface,
// but we return Doctor, Programmer, etc.
public class IdentityProvider {

    // Create profession by name, uses simple if-else (beginner style)
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
