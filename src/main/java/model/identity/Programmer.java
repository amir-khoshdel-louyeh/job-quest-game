package model.identity;

/**
 * Identity subclass representing a Programmer profession and its specific properties.
 *
 * Follows Single Responsibility Principle by modelling the programmer identity only.
 */

import model.Identity;

public class Programmer implements Identity {
    @Override
    public String getProfessionName() { return "Programmer"; }
}
