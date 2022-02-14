package ViewModel;

import Model.Direction;
import Model.ElevatorState.ElevatorState;

import java.util.List;

public interface SimulationViewModel {
    ElevatorState[] getElevatorStates();
    void step();
    void pressedFloorButton(int id, Direction direction);
    void pressedElevatorCanvas(int elevator, int floor);
    List<Integer> getGeneralTargets(Direction direction);
    List<Integer> getSpecificTargets(int elevatorId);
}
