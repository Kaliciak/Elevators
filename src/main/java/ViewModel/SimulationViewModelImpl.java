package ViewModel;

import Model.ElevatorState;
import Model.ElevatorSystem;
import Model.ElevatorSystemImpl;

public class SimulationViewModelImpl implements SimulationViewModel {
    // TODO: injection
    private ElevatorSystem elevatorSystem = new ElevatorSystemImpl();

    @Override
    public ElevatorState[] getElevatorStates() {
        return elevatorSystem.getElevatorStates();
    }
}
