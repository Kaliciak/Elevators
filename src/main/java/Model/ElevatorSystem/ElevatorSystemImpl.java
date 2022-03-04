package Model.ElevatorSystem;

import Model.Direction;
import Model.Elevator.Elevator;
import Model.ElevatorState.ElevatorState;
import Model.TargetManager.TargetManager;

import java.util.ArrayList;
import java.util.List;

public class ElevatorSystemImpl implements ElevatorSystem {
    private int elevatorsCount;
    private Elevator[] elevators;
    private TargetManager targetManager;

    public ElevatorSystemImpl(int elevatorsCount, Elevator[] elevators, TargetManager targetManager) {
        this.elevatorsCount = elevatorsCount;
        this.elevators = elevators;
        this.targetManager = targetManager;
    }

    @Override
    public ElevatorState[] getElevatorStates() {
        ArrayList<ElevatorState> states = new ArrayList<>();

        for(Elevator elevator: elevators) {
            states.add(elevator.getState());
        }

        return states.toArray(ElevatorState[]::new);
    }

    @Override
    public void step() {
        for(Elevator elevator: elevators) {
            elevator.step();
        }
    }

    @Override
    public List<Integer> getGeneralTargets(Direction direction) {
        return targetManager.getGeneralTargets(direction);
    }

    @Override
    public List<Integer> getSpecificTargets(int elevatorId) {
        return targetManager.getSpecificTargets(elevatorId);
    }

    @Override
    public void changeElevatorStop(int index) {
        targetManager.changeElevatorStop(index);
    }

    @Override
    public void generalRequest(int floor, Direction direction) {
        targetManager.generalRequest(floor, direction);
    }

    @Override
    public void specificRequest(int floor, int elevatorID) {
        targetManager.specificRequest(floor, elevatorID);
    }


}
