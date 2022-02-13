package Model.Elevator;

import Model.Direction;
import Model.ElevatorState.ElevatorState;
import Model.TargetManager.TargetManagerDelegate;

public interface Elevator {
    ElevatorState getState();
    Direction getDirection();
    void setTarget(Integer floor);
    void step();
    void setDelegate(TargetManagerDelegate delegate);
}
