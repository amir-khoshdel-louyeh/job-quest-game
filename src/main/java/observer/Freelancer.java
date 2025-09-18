package model.identity;

import model.Identity;
import model.WorkResult;

public class Freelancer implements Identity {
    @Override
    public WorkResult performDailyWork() {
        return new WorkResult(0, 0, WorkResult.Type.REQUIRES_DIALOG);
    }
}