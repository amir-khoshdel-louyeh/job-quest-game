package provider;

// Unknown profession name.
// Unchecked: a wrong key wired into the UI or registry.
public final class UnknownProfessionException extends IllegalArgumentException {
    UnknownProfessionException(String professionName) {
        super("Unknown profession type: " + professionName);
    }
}
