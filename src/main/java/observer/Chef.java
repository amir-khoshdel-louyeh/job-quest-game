package model.identity;

import model.Identity;
import model.WorkResult;

public class Chef implements Identity {
    @Override
    public WorkResult performDailyWork() {
        return new WorkResult(250, 10000, WorkResult.Type.INSTANT_EARNING);
    }
}