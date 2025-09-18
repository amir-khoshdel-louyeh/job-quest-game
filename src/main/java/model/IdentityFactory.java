package model;

import model.identity.Doctor;
import model.identity.LogoDesigner;
import model.identity.Programmer;
import model.identity.Typist;

/**
 * Factory class for creating instances of Identity subclasses.
 * This centralizes the creation logic, making it easier to add new identities
 * without modifying the views that use them.
 */
public class IdentityFactory {

    /**
     * Creates an Identity object based on its name.
     * @param identityName The simple class name of the Identity (e.g., "Freelancer").
     * @return An instance of the corresponding Identity subclass.
     * @throws IllegalArgumentException if the identityName is unknown.
     */
    public static Identity createIdentity(String identityName) {
        switch (identityName) {
            case "Doctor":
                return new Doctor();
            case "Programmer":
                return new Programmer();
            case "LogoDesigner":
                return new LogoDesigner();
            case "Typist":
                return new Typist();
            default:
                // Or return a default, but throwing an exception is often cleaner.
                throw new IllegalArgumentException("Unknown identity type: " + identityName);
        }
    }
}