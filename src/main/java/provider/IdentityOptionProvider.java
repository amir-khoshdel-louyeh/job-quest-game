package provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Identity;

// Holds the 4 starting professions plus card artwork.
// Read by StartPanel.
public class IdentityOptionProvider {

    private final ArrayList<Identity> options = new ArrayList<Identity>();

    private final Map<String, String> imagePaths = new HashMap<String, String>();

    // ----- Construction -----
    // Identities come from outside.
    public IdentityOptionProvider() {
        this(new IdentityProvider());
    }

    public IdentityOptionProvider(IdentityProvider identities) {
        if (identities == null) {
            throw new IllegalArgumentException("identities must not be null");
        }
        options.add(identities.createIdentity("Doctor"));
        options.add(identities.createIdentity("Programmer"));
        options.add(identities.createIdentity("LogoDesigner"));
        options.add(identities.createIdentity("Typist"));

        imagePaths.put("Doctor", "/images/doctor.png");
        imagePaths.put("Programmer", "/images/programmer.png");
        imagePaths.put("LogoDesigner", "/images/logodesigner.png");
        imagePaths.put("Typist", "/images/typist.png");
    }

    // ----- Getter -----
    public List<Identity> getAvailableOptions() {
        ArrayList<Identity> copy = new ArrayList<Identity>();
        for (int i = 0; i < options.size(); i++) {
            copy.add(options.get(i));
        }
        return copy;
    }

    // ----- Queries -----
    // Card artwork for an option, or null when it has none.
    public String imagePathFor(Identity option) {
        if (option == null) {
            return null;
        }
        return imagePaths.get(option.getProfessionName());
    }
}
