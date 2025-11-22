package provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Identity;

// Holds the 4 starting professions the player can choose from.
// Also owns their card artwork: image paths are presentation data for the
// option screen, so they live here — not in the domain Identity.
// Instance repository (not static holder) so StartPanel can inject/mock it.
public class IdentityOptionProvider {

    private final ArrayList<Identity> options = new ArrayList<Identity>();

    private final Map<String, String> imagePaths = new HashMap<String, String>();

    public IdentityOptionProvider() {
        this(new IdentityProvider());
    }

    // Test seam: identities come from outside (testable, swappable).
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

    public List<Identity> getAvailableOptions() {
        ArrayList<Identity> copy = new ArrayList<Identity>();
        for (int i = 0; i < options.size(); i++) {
            copy.add(options.get(i));
        }
        return copy;
    }

    // Card artwork for an option, or null when it has none.
    public String imagePathFor(Identity option) {
        if (option == null) {
            return null;
        }
        return imagePaths.get(option.getProfessionName());
    }
}
