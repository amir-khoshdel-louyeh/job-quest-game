package model.identity;

/**
 * Identity subclass representing a Doctor profession with its baseline stats.
 *
 * Follows Single Responsibility Principle by modelling the doctor identity only.
 */

import model.Identity;
import model.WorkResult;

public class Doctor implements Identity {
    
    public WorkResult performDailyWork() {
        // perform daily work action for Doctor identity
        return new WorkResult(0, 0, WorkResult.Type.REQUIRES_DIALOG);
    }
}