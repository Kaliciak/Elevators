package Model.TargetManager;

import Model.Direction;

public interface TargetManager extends TargetManagerDelegate {
    void generalRequest(int floor, Direction direction);
    void specificRequest(int floor, int elevatorID);
}
