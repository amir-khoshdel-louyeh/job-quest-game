package provider;

import model.Identity;
import model.identity.Doctor;
import model.identity.LogoDesigner;
import model.identity.Programmer;
import model.identity.Typist;

/**
 * Provider/factory class for creating instances of Identity subclasses.
 * This centralizes the creation logic, making it easier to add new identities
 * without modifying the views that use them.
 */
public class IdentityProvider {

    /** Create an Identity instance based on its name */
    public static Identity createIdentity(String identityName) {
        if (identityName.equals("Doctor")) {
            return new Doctor();
        } else if (identityName.equals("Programmer")) {
            return new Programmer();
        } else if (identityName.equals("LogoDesigner") || identityName.equals("Logo Designer")) {
            return new LogoDesigner();
        } else if (identityName.equals("Typist")) {
            return new Typist();
        } else {
            // Or return a default, but throwing an exception is often cleaner.
            throw new IllegalArgumentException("Unknown identity type: " + identityName);
        }
    }
}
