package provider;

import provider.contract.ProfessionCatalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import model.career.Profession;
import model.career.ProfessionDefinition;
import utils.Lists;

// The profession registry: creates any career by name and lists the four
// starting cards (with artwork) the start screen shows.
// Keys ignore case and spaces.
public final class ProfessionRegistry implements ProfessionCatalog {
    private final Map<String, Supplier<Profession>> registry = new HashMap<String, Supplier<Profession>>();
    private final ArrayList<Profession> options = new ArrayList<Profession>();
    private final Map<String, String> imagePaths = new HashMap<String, String>();

    // ----- Construction -----
    public ProfessionRegistry() {
        registry.put("doctor", () -> new ProfessionDefinition("Doctor", "Doctor", "Heal players and earn big money.",
                500));
        registry.put("programmer", () -> new ProfessionDefinition("Programmer", "Programmer", "Write code and solve complex problems.",
                300));
        registry.put("logodesigner", () -> new ProfessionDefinition("LogoDesigner", "Logo Designer", "Create logos for clients.",
                200));
        registry.put("typist", () -> new ProfessionDefinition("Typist", "Typist", "Type documents for clients.",
                200));
        addStartingOptions();
    }

    // One card per starting career, plus the artwork shown on it.
    private void addStartingOptions() {
        options.add(createProfession("Doctor"));
        options.add(createProfession("Programmer"));
        options.add(createProfession("LogoDesigner"));
        options.add(createProfession("Typist"));

        imagePaths.put("Doctor", "/images/doctor.png");
        imagePaths.put("Programmer", "/images/programmer.png");
        imagePaths.put("LogoDesigner", "/images/logodesigner.png");
        imagePaths.put("Typist", "/images/typist.png");
    }

    // ----- Queries -----
    @Override
    public Profession createProfession(String professionName) {
        if (professionName == null) {
            throw new UnknownProfessionException(null);
        }
        Supplier<Profession> factory = registry.get(normalize(professionName));
        if (factory == null) {
            throw new UnknownProfessionException(professionName);
        }
        return factory.get();
    }

    // The four starting careers, in card order.
    @Override
    public List<Profession> getAll() {
        return Lists.copyOf(options);
    }

    // Card artwork for an option, or null when it has none.
    @Override
    public String imagePathFor(Profession option) {
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
