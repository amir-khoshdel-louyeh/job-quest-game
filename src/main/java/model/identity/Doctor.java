package model.identity;

import model.Identity;
import model.WorkResult;

public class Doctor implements Identity {
    
    public WorkResult performDailyWork() {
        return new WorkResult(0, 0, WorkResult.Type.REQUIRES_DIALOG);
    }
}