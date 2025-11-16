package provider;

import java.util.ArrayList;
import java.util.List;

import model.Identity;

// Holds the 4 starting professions the player can choose from.
public class IdentityOptionProvider {

    private static final ArrayList<Identity> OPTIONS = new ArrayList<Identity>();

    static {
        OPTIONS.add(IdentityProvider.createIdentity("Doctor"));
        OPTIONS.add(IdentityProvider.createIdentity("Programmer"));
        OPTIONS.add(IdentityProvider.createIdentity("LogoDesigner"));
        OPTIONS.add(IdentityProvider.createIdentity("Typist"));
    }

    public static List<Identity> getAvailableOptions() {
        ArrayList<Identity> copy = new ArrayList<Identity>();
        for (int i = 0; i < OPTIONS.size(); i++) {
            copy.add(OPTIONS.get(i));
        }
        return copy;
    }
}
