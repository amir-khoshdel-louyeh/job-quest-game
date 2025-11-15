package model.identity;

// The programmer profession. Knows its own data.
public class Programmer extends BaseIdentity {
    public static final String PROFESSION_NAME = "Programmer";
    public static final String DISPLAY_NAME = "Programmer";
    public static final String GAME_KIND = "CODE";

    @Override
    public String getProfessionName() { return PROFESSION_NAME; }

    @Override
    public String getDisplayName() { return DISPLAY_NAME; }

    @Override
    public String getDescription() { return "Write code and solve complex problems."; }

    @Override
    public int getStartingBalance() { return 300; }

    @Override
    public String getImagePath() { return "images/programmer.png"; }

    @Override
    public String getWorkGameKind() { return GAME_KIND; }
}
