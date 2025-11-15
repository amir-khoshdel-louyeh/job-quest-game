package model.identity;

// The logo designer profession. Knows its own data.
public class LogoDesigner extends BaseIdentity {
    public static final String PROFESSION_NAME = "LogoDesigner";
    public static final String DISPLAY_NAME = "Logo Designer";
    public static final String GAME_KIND = "DESIGN";

    @Override
    public String getProfessionName() { return PROFESSION_NAME; }

    @Override
    public String getDisplayName() { return DISPLAY_NAME; }

    @Override
    public String getDescription() { return "Create logos for clients."; }

    @Override
    public int getStartingBalance() { return 200; }

    @Override
    public String getImagePath() { return "/images/logodesigner.png"; }

    @Override
    public String getWorkGameKind() { return GAME_KIND; }
}
