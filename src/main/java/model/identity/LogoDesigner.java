package model.identity;

/**
 * Identity subclass representing a Logo Designer profession and related defaults.
 *
 * Follows Single Responsibility Principle by modelling the logo-designer identity only.
 */

import model.Identity;

public class LogoDesigner implements Identity {
    @Override
    public String getProfessionName() { return "LogoDesigner"; }
}
