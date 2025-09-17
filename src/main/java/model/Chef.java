package model;

public class Chef implements Identity {
    @Override
    public WorkResult performDailyWork() {
        // Chefs earn a fixed amount for a fixed energy cost.
        return new WorkResult(250, 10000, WorkResult.Type.INSTANT_EARNING);
    }
}