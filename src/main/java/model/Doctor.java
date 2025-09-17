package model;

public class Doctor implements Identity {
    @Override
    public WorkResult performDailyWork() {
        // Doctors earn a high fixed amount for a high energy cost.
        return new WorkResult(500, 15000, WorkResult.Type.INSTANT_EARNING);
    }
}