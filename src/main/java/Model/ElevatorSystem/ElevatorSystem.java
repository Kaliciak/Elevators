package Model.ElevatorSystem;

import Model.Direction;
import Model.ElevatorState.ElevatorState;

import java.util.List;

public interface ElevatorSystem {
    ElevatorState[] getElevatorStates();
    void generalRequest(int floor, Direction direction);
    void specificRequest(int floor, int elevatorID);
    void step();
    List<Integer> getGeneralTargets(Direction direction);
    List<Integer> getSpecificTargets(int elevatorId);
}
