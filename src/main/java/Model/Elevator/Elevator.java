package Model.Elevator;

import Model.Direction;
import Model.ElevatorState.ElevatorState;
import Model.ElevatorState.Target;
import Model.TargetManager.TargetManagerDelegate;

public interface Elevator {
    ElevatorState getState();
    Direction getDirection();
    void setTarget(Target target);
    void step();
    void setDelegate(TargetManagerDelegate delegate);
}
