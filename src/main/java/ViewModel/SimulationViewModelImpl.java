package ViewModel;

import Model.Direction;
import Model.Elevator.Elevator;
import Model.ElevatorState.ElevatorState;
import Model.ElevatorState.ElevatorStateImpl;
import Model.ElevatorSystem.ElevatorSystem;
import Model.Fabrics.SystemFabric;
import Model.Fabrics.SystemFabricImpl;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationViewModelImpl implements SimulationViewModel {
    // TODO: injection
    private ElevatorSystem elevatorSystem;

    public SimulationViewModelImpl() {
        SystemFabric systemFabric = new SystemFabricImpl();
        elevatorSystem = systemFabric.createElevatorSystem(5);
    }

    @Override
    public ElevatorState[] getElevatorStates() {
        return Arrays.stream(elevatorSystem.getElevatorStates()).
                map(this::toIndexedFloorState).
                collect(Collectors.toList()).toArray(ElevatorState[]::new);
    }

    @Override
    public void step() {
        elevatorSystem.step();
    }

    @Override
    public void pressedFloorButton(int id, Direction direction) {
        elevatorSystem.generalRequest(5 - id, direction);
    }

    @Override
    public void pressedElevatorCanvas(int elevator, int floor) {
        elevatorSystem.specificRequest(5 - floor, elevator);
    }

    //TODO: translate floor number to floor id
    @Override
    public List<Integer> getGeneralTargets(Direction direction) {
        return elevatorSystem.getGeneralTargets(direction).stream().map(this::getFloorIndex).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSpecificTargets(int elevatorId) {
        return elevatorSystem.getSpecificTargets(elevatorId).stream().map(this::getFloorIndex).collect(Collectors.toList());
    }

    private Integer getFloorIndex(Integer floor) {
        return (floor == null) ? null : 5 - floor;
    }

    private ElevatorState toIndexedFloorState(ElevatorState state) {
        return new ElevatorStateImpl(state.getID(), getFloorIndex(state.getFloor()), getFloorIndex(state.getTarget()));
    }
}
