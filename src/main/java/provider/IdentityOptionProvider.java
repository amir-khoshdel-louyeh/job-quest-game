package provider;

import java.util.ArrayList;
import java.util.List;

import model.IdentityOption;
import model.identity.Doctor;
import model.identity.LogoDesigner;
import model.identity.Programmer;
import model.identity.Typist;

// Holds the 4 starting professions the player can choose from.
// Data (price, description...) lives inside each Identity class,
// here we only list which professions exist.
public class IdentityOptionProvider {

    private static final ArrayList<IdentityOption> OPTIONS = new ArrayList<IdentityOption>();

    static {
        OPTIONS.add(new IdentityOption(new Doctor()));
        OPTIONS.add(new IdentityOption(new Programmer()));
        OPTIONS.add(new IdentityOption(new LogoDesigner()));
        OPTIONS.add(new IdentityOption(new Typist()));
    }

    public static List<IdentityOption> getAvailableOptions() {
        ArrayList<IdentityOption> copy = new ArrayList<IdentityOption>();
        for (int i = 0; i < OPTIONS.size(); i++) {
            copy.add(OPTIONS.get(i));
        }
        return copy;
    }
}
