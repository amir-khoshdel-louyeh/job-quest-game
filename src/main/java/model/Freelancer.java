package model;

public class Freelancer implements Identity {
    @Override
    public WorkResult performDailyWork() {
        // Freelancer work is not instant; it requires opening a task dialog.
        return new WorkResult(0, 0, WorkResult.Type.REQUIRES_DIALOG);
    }
}