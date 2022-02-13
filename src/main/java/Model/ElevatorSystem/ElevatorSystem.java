package Model.ElevatorSystem;

import Model.Direction;
import Model.ElevatorState.ElevatorState;

public interface ElevatorSystem {
    ElevatorState[] getElevatorStates();
    void generalRequest(int floor, Direction direction);
    void specificRequest(int floor, int elevatorID);
    void step();
}
