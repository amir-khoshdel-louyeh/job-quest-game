package provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Identity;

// Holds the 4 starting professions the player can choose from.
// Also owns their card artwork: image paths are presentation data for the
// option screen, so they live here — not in the domain Identity.
public class IdentityOptionProvider {

    private static final ArrayList<Identity> OPTIONS = new ArrayList<Identity>();

    private static final Map<String, String> IMAGE_PATHS = new HashMap<String, String>();

    static {
        OPTIONS.add(IdentityProvider.createIdentity("Doctor"));
        OPTIONS.add(IdentityProvider.createIdentity("Programmer"));
        OPTIONS.add(IdentityProvider.createIdentity("LogoDesigner"));
        OPTIONS.add(IdentityProvider.createIdentity("Typist"));

        IMAGE_PATHS.put("Doctor", "/images/doctor.png");
        IMAGE_PATHS.put("Programmer", "/images/programmer.png");
        IMAGE_PATHS.put("LogoDesigner", "/images/logodesigner.png");
        IMAGE_PATHS.put("Typist", "/images/typist.png");
    }

    public static List<Identity> getAvailableOptions() {
        ArrayList<Identity> copy = new ArrayList<Identity>();
        for (int i = 0; i < OPTIONS.size(); i++) {
            copy.add(OPTIONS.get(i));
        }
        return copy;
    }

    // Card artwork for an option, or null when it has none.
    public static String imagePathFor(Identity option) {
        if (option == null) {
            return null;
        }
        return IMAGE_PATHS.get(option.getProfessionName());
    }
}
