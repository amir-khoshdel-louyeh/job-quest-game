package provider;

import java.util.ArrayList;
import java.util.List;

import model.IdentityOption;

// Holds the 4 starting professions the player can choose from.
public class IdentityOptionProvider {

    private static ArrayList<IdentityOption> OPTIONS = new ArrayList<IdentityOption>();

    static {
        OPTIONS.add(new IdentityOption("Doctor", "Heal players and earn big money.", 500, "images/doctor.png"));
        OPTIONS.add(new IdentityOption("Programmer", "Write code and solve complex problems.", 300, "images/programmer.png"));
        OPTIONS.add(new IdentityOption("Logo Designer", "Create logos for clients.", 200, "images/logodesigner.png"));
        OPTIONS.add(new IdentityOption("Typist", "Type documents for clients.", 200, "images/typist.png"));
    }

    public static List<IdentityOption> getAvailableOptions() {
        ArrayList<IdentityOption> copy = new ArrayList<IdentityOption>();
        for (int i = 0; i < OPTIONS.size(); i++) {
            copy.add(OPTIONS.get(i));
        }
        return copy;
    }
}
