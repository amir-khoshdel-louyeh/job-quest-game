package provider;

// Unknown profession name.
// Unchecked: a wrong key wired into the UI or registry.
public class UnknownIdentityException extends IllegalArgumentException {
    public UnknownIdentityException(String identityName) {
        super("Unknown identity type: " + identityName);
    }
}
