package Model.TargetManager;

import Model.Direction;

import java.util.List;

public interface TargetManager extends TargetManagerDelegate {
    void generalRequest(int floor, Direction direction);
    void specificRequest(int floor, int elevatorID);
    List<Integer> getGeneralTargets(Direction direction);
    List<Integer> getSpecificTargets(int elevatorId);
}
