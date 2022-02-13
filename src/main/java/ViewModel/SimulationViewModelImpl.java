package ViewModel;

import Model.ElevatorState.ElevatorState;
import Model.ElevatorSystem.ElevatorSystem;
import Model.Fabrics.SystemFabric;
import Model.Fabrics.SystemFabricImpl;

public class SimulationViewModelImpl implements SimulationViewModel {
    // TODO: injection
    private ElevatorSystem elevatorSystem;

    public SimulationViewModelImpl() {
        SystemFabric systemFabric = new SystemFabricImpl();
        elevatorSystem = systemFabric.createElevatorSystem(5);
    }

    @Override
    public ElevatorState[] getElevatorStates() {
        return elevatorSystem.getElevatorStates();
    }

    @Override
    public void step() {
        elevatorSystem.step();
    }
}
