package model.player;

// Composite upkeep surface used by RoutineService. Individual rules depend on
// the smaller VitalsTarget/KnockoutTarget/SicknessTarget ports instead.
public interface UpkeepTarget extends DrainTarget, SicknessTarget {
}
