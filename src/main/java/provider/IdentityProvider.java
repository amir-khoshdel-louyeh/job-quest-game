package provider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import model.Identity;
import model.WorkGameKind;
import model.career.Profession;

// Builds the starting profession the player picks.
// Instance registry (Factory): each profession registers one map entry,
// so adding a new one never edits the createIdentity dispatch (OCP).
// Keys are normalized (trim + lowercase + no spaces), so "LogoDesigner"
// and "Logo Designer" (old save compatibility) resolve to the same entry.
public class IdentityProvider {
    private final Map<String, Supplier<Identity>> registry = new HashMap<String, Supplier<Identity>>();

    public IdentityProvider() {
        this(defaultRegistry());
    }

    // Test seam: custom registry makes every run reproducible.
    public IdentityProvider(Map<String, Supplier<Identity>> registry) {
        if (registry == null) {
            throw new IllegalArgumentException("registry must not be null");
        }
        this.registry.putAll(registry);
    }

    private static Map<String, Supplier<Identity>> defaultRegistry() {
        Map<String, Supplier<Identity>> defaults = new HashMap<String, Supplier<Identity>>();
        defaults.put("doctor", () -> new Profession("Doctor", "Doctor", "Heal players and earn big money.",
                500, WorkGameKind.DIAGNOSIS));
        defaults.put("programmer", () -> new Profession("Programmer", "Programmer", "Write code and solve complex problems.",
                300, WorkGameKind.CODE));
        defaults.put("logodesigner", () -> new Profession("LogoDesigner", "Logo Designer", "Create logos for clients.",
                200, WorkGameKind.DESIGN));
        defaults.put("typist", () -> new Profession("Typist", "Typist", "Type documents for clients.",
                200, WorkGameKind.TYPING));
        return defaults;
    }

    public Identity createIdentity(String identityName) {
        if (identityName == null) {
            throw new IllegalArgumentException("Unknown identity type: null");
        }
        Supplier<Identity> factory = registry.get(normalize(identityName));
        if (factory == null) {
            throw new IllegalArgumentException("Unknown identity type: " + identityName);
        }
        return factory.get();
    }

    // "Logo Designer" -> "logodesigner"; lookup is case/space-insensitive.
    private static String normalize(String raw) {
        String trimmed = raw.trim().toLowerCase();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c != ' ' && c != '\t') {
                key.append(c);
            }
        }
        return key.toString();
    }
}
