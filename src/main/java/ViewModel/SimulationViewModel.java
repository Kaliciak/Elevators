package ViewModel;

import Model.ElevatorState.ElevatorState;

public interface SimulationViewModel {
    ElevatorState[] getElevatorStates();
    void step();
}
