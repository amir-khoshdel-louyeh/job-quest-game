package model.identity;

/**
 * Identity subclass representing a Logo Designer profession and related defaults.
 *
 * Follows Single Responsibility Principle by modelling the logo-designer identity only.
 */

import model.Identity;
import model.WorkResult;

public class LogoDesigner implements Identity {
    
    public WorkResult performDailyWork() {
        // perform daily work action for LogoDesigner identity
        return new WorkResult(0, 0, WorkResult.Type.REQUIRES_DIALOG);
    }
}
