package ViewModel.Simulation;

import Model.Direction;
import Model.ElevatorState.ElevatorState;
import Model.ElevatorState.ElevatorStateImpl;
import Model.ElevatorState.Target;
import Model.ElevatorState.TargetImpl;
import Model.ElevatorSystem.ElevatorSystem;
import Model.Fabrics.SystemFabric;
import Model.Fabrics.SystemFabricImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationViewModelImpl implements SimulationViewModel {
    private ElevatorSystem elevatorSystem;
    private int maxFloor;

    public SimulationViewModelImpl(int elevatorsCount, int maxFloor, int floorsCount) {
        SystemFabric systemFabric = new SystemFabricImpl();
        this.maxFloor = maxFloor;
        elevatorSystem = systemFabric.createElevatorSystem(elevatorsCount);
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
        elevatorSystem.generalRequest(getFloorIndex(id), direction);
    }

    @Override
    public void pressedElevatorCanvas(int elevator, int floor) {
        elevatorSystem.specificRequest(getFloorIndex(floor), elevator);
    }

    @Override
    public List<Integer> getGeneralTargets(Direction direction) {
        return elevatorSystem.getGeneralTargets(direction).stream().map(this::getFloorIndex).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSpecificTargets(int elevatorId) {
        return elevatorSystem.getSpecificTargets(elevatorId).stream().map(this::getFloorIndex).collect(Collectors.toList());
    }

    private Integer getFloorIndex(Integer floor) {
        return (floor == null) ? null : maxFloor - floor;
    }

    private ElevatorState toIndexedFloorState(ElevatorState state) {
        TargetImpl newTarget = null;
        Target oldTarget = state.getTarget();
        if(oldTarget != null) {
            newTarget = new TargetImpl(getFloorIndex(oldTarget.getFloor()), oldTarget.isSpecific(), oldTarget.isUp(), oldTarget.isDown());
        }
        return new ElevatorStateImpl(state.getID(), getFloorIndex(state.getFloor()), newTarget, state.isDoorOpen());
    }

    public void pressedElevatorStop(int index) {
        elevatorSystem.changeElevatorStop(index);
    }
}
