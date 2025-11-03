package model.identity;

/**
 * Identity subclass representing a Programmer profession and its specific properties.
 *
 * Follows Single Responsibility Principle by modelling the programmer identity only.
 */

import model.Identity;
import model.WorkResult;

public class Programmer implements Identity {
    
    public WorkResult performDailyWork() {
        // perform daily work action for Programmer identity
        return new WorkResult(0, 0, WorkResult.Type.REQUIRES_DIALOG);
    }
}
