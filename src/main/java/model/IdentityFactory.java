package model;

import model.identity.Chef;
import model.identity.Doctor;
import model.identity.Freelancer;

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
            case "Freelancer":
                return new Freelancer();
            case "Chef":
                return new Chef();
            case "Doctor":
                return new Doctor();
            default:
                // Or return a default, but throwing an exception is often cleaner.
                throw new IllegalArgumentException("Unknown identity type: " + identityName);
        }
    }
}