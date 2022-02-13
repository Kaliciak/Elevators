package ViewModel;

import Model.Direction;
import Model.ElevatorState.ElevatorState;

public interface SimulationViewModel {
    ElevatorState[] getElevatorStates();
    void step();
    void pressedFloorButton(int id, Direction direction);
}
