package model.identity;

/**
 * Identity subclass representing a Typist profession and its default attributes.
 *
 * Follows Single Responsibility Principle by modelling the typist identity only.
 */

import model.Identity;
import model.WorkResult;

public class Typist implements Identity {
    
    public WorkResult performDailyWork() {
        // perform daily work action for Typist identity
        return new WorkResult(0, 0, WorkResult.Type.REQUIRES_DIALOG);
    }
}
