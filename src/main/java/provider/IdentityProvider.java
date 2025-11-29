package provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import model.Identity;
import model.career.Profession;
import utils.Lists;

// The profession registry: creates any career by name and lists the four
// starting cards (with artwork) the start screen shows.
// Keys ignore case and spaces.
public final class IdentityProvider {
    private final Map<String, Supplier<Identity>> registry = new HashMap<String, Supplier<Identity>>();
    private final ArrayList<Identity> options = new ArrayList<Identity>();
    private final Map<String, String> imagePaths = new HashMap<String, String>();

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
        addStartingOptions();
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

    // One card per starting career, plus the artwork shown on it.
    private void addStartingOptions() {
        if (!registry.containsKey("doctor")) {
            return; // custom registry: no cards to show
        }
        options.add(createIdentity("Doctor"));
        options.add(createIdentity("Programmer"));
        options.add(createIdentity("LogoDesigner"));
        options.add(createIdentity("Typist"));

        imagePaths.put("Doctor", "/images/doctor.png");
        imagePaths.put("Programmer", "/images/programmer.png");
        imagePaths.put("LogoDesigner", "/images/logodesigner.png");
        imagePaths.put("Typist", "/images/typist.png");
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

    // The four starting careers, in card order.
    public List<Identity> getAvailableOptions() {
        return Lists.copyOf(options);
    }

    // Card artwork for an option, or null when it has none.
    public String imagePathFor(Identity option) {
        if (option == null) {
            return null;
        }
        return imagePaths.get(option.getProfessionName());
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
