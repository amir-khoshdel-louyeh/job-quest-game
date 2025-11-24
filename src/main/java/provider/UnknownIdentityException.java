package provider;

// Domain exception: an unknown profession name was requested.
// Unchecked by design: it signals a programming/config error (wrong key
// wired into the UI or registry), so callers fail fast like any other
// IllegalArgumentException — and existing catch (IllegalArgumentException)
// sites keep working via polymorphism (a catch on the superclass catches
// the subclass). UI layers may add a specific catch branch first for a
// tailored message (see StartPanel.startGame: specific-before-general).
public class UnknownIdentityException extends IllegalArgumentException {
    public UnknownIdentityException(String identityName) {
        super("Unknown identity type: " + identityName);
    }
}
