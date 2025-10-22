package provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.IdentityOption;

/**
 * Provides a centralized, single source of truth for available Identity options in the game.
 * This decouples the UI from the game's data configuration.
 */
public class IdentityOptionProvider {

    private static final List<IdentityOption> OPTIONS = new ArrayList<>();

    static {
        // To add a new character, just add a new line here.
        // The registration panel will automatically display it.
        OPTIONS.add(new IdentityOption("Doctor", "Heal players and earn big money.", 10000, "images/doctor.png"));
        OPTIONS.add(new IdentityOption("Programmer", "Write code and solve complex problems.", 100, "images/programmer.png"));
        OPTIONS.add(new IdentityOption("Logo Designer", "Create logos for clients.", 10, "images/logodesigner.png"));
        OPTIONS.add(new IdentityOption("Typist", "Type documents for clients.", 10, "images/typist.png"));

    }

    public static List<IdentityOption> getAvailableOptions() {
        return Collections.unmodifiableList(OPTIONS);
    }
}
