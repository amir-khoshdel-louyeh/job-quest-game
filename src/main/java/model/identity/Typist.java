package model.identity;

// The typist profession. Knows its own data.
public class Typist extends BaseIdentity {
    public static final String PROFESSION_NAME = "Typist";
    public static final String DISPLAY_NAME = "Typist";
    public static final String GAME_KIND = "TYPING";

    @Override
    public String getProfessionName() { return PROFESSION_NAME; }

    @Override
    public String getDisplayName() { return DISPLAY_NAME; }

    @Override
    public String getDescription() { return "Type documents for clients."; }

    @Override
    public int getStartingBalance() { return 200; }

    @Override
    public String getImagePath() { return "images/typist.png"; }

    @Override
    public String getWorkGameKind() { return GAME_KIND; }
}
