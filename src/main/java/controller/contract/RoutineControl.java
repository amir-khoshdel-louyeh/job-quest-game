package controller.contract;

// Start/stop switch for the periodic upkeep.
// GamePanel depends on this contract, not on RoutineController.
public interface RoutineControl {

    void startSchedulers();

    void stopSchedulers();
}
