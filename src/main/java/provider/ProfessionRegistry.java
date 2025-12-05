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
        register("Doctor", "Doctor", "Heal players and earn big money.", 500, "/images/doctor.png");
        register("Programmer", "Programmer", "Write code and solve complex problems.", 300, "/images/programmer.png");
        register("LogoDesigner", "Logo Designer", "Create logos for clients.", 200, "/images/logodesigner.png");
        register("Typist", "Typist", "Type documents for clients.", 200, "/images/typist.png");
    }

    // Adds one starting career, in card order, to the lookup and artwork indexes.
    private void register(String name, String displayName, String description, int startingBalance, String imagePath) {
        String key = normalize(name);
        Supplier<Profession> factory = () -> new ProfessionDefinition(name, displayName, description, startingBalance);
        registry.put(key, factory);
        options.add(factory.get());
        imagePaths.put(key, imagePath);
    }

    // ----- Queries -----
    @Override
    public Profession createProfession(String professionName) {
        return buildProfession(professionName);
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
        return imagePaths.get(normalize(option.getProfessionName()));
    }

    // ----- Private helpers -----
    private Profession buildProfession(String professionName) {
        if (professionName == null) {
            throw new UnknownProfessionException(null);
        }
        Supplier<Profession> factory = registry.get(normalize(professionName));
        if (factory == null) {
            throw new UnknownProfessionException(professionName);
        }
        return factory.get();
    }

    // "Logo Designer" -> "logodesigner".
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
