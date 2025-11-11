package model.identity;

/**
 * Identity subclass representing a Typist profession and its default attributes.
 *
 * Follows Single Responsibility Principle by modelling the typist identity only.
 */

import model.Identity;

public class Typist implements Identity {
    @Override
    public String getProfessionName() { return "Typist"; }
}
