package model.identity;

import model.Identity;
import model.WorkResult;

public class Doctor implements Identity {
    @Override
    public WorkResult performDailyWork() {
        return new WorkResult(500, 15000, WorkResult.Type.INSTANT_EARNING);
    }
}