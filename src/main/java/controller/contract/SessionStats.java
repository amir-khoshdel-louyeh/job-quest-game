package controller.contract;

// Session numbers the controller records and the query layer reports.
// Both depend on this contract, not on SessionTracker.
public interface SessionStats {

    void recordCompletedJob();

    String summary();
}
