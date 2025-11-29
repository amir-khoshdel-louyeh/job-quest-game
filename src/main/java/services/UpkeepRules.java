package services;

import model.player.UpkeepTarget;

// The periodic upkeep operations RoutineController needs.
// RoutineController depends on this contract, not on RoutineService.
public interface UpkeepRules {

    String processSicknessCheck(UpkeepTarget player);

    String processEnergyDecrease(UpkeepTarget player);

    String processHealthDecrease(UpkeepTarget player);
}
