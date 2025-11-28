package provider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import model.Identity;
import model.career.Profession;

// Builds the starting profession the player picks.
// Keys ignore case and spaces.
public class IdentityProvider {
    private final Map<String, Supplier<Identity>> registry = new HashMap<String, Supplier<Identity>>();

    // ----- Construction -----
    public IdentityProvider() {
        this(defaultRegistry());
    }

    // Custom registry for tests.
    public IdentityProvider(Map<String, Supplier<Identity>> registry) {
        if (registry == null) {
            throw new IllegalArgumentException("registry must not be null");
        }
        this.registry.putAll(registry);
    }

    private static Map<String, Supplier<Identity>> defaultRegistry() {
        Map<String, Supplier<Identity>> defaults = new HashMap<String, Supplier<Identity>>();
        defaults.put("doctor", () -> new Profession("Doctor", "Doctor", "Heal players and earn big money.",
                500));
        defaults.put("programmer", () -> new Profession("Programmer", "Programmer", "Write code and solve complex problems.",
                300));
        defaults.put("logodesigner", () -> new Profession("LogoDesigner", "Logo Designer", "Create logos for clients.",
                200));
        defaults.put("typist", () -> new Profession("Typist", "Typist", "Type documents for clients.",
                200));
        return defaults;
    }

    // ----- Queries -----
    public Identity createIdentity(String identityName) {
        if (identityName == null) {
            throw new UnknownIdentityException(null);
        }
        Supplier<Identity> factory = registry.get(normalize(identityName));
        if (factory == null) {
            throw new UnknownIdentityException(identityName);
        }
        return factory.get();
    }

    // "Logo Designer" -> "logodesigner".
    // ----- Private helpers -----
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
