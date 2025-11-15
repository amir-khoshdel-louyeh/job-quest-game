package model.identity;

// The doctor profession. Knows its own data.
public class Doctor extends BaseIdentity {
    public static final String PROFESSION_NAME = "Doctor";
    public static final String DISPLAY_NAME = "Doctor";
    public static final String GAME_KIND = "DIAGNOSIS";

    @Override
    public String getProfessionName() { return PROFESSION_NAME; }

    @Override
    public String getDisplayName() { return DISPLAY_NAME; }

    @Override
    public String getDescription() { return "Heal players and earn big money."; }

    @Override
    public int getStartingBalance() { return 500; }

    @Override
    public String getImagePath() { return "images/doctor.png"; }

    @Override
    public String getWorkGameKind() { return GAME_KIND; }
}
