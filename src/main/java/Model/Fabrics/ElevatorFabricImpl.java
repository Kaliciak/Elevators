package Model.Fabrics;

import Model.Elevator.Elevator;
import Model.Elevator.ElevatorImpl;
import Model.ElevatorState.ElevatorStateImpl;
import Model.ElevatorState.MutableElevatorState;

public class ElevatorFabricImpl implements ElevatorFabric {

    @Override
    public Elevator createElevator(int id) {
        MutableElevatorState state = new ElevatorStateImpl(id, 0, null);
        return new ElevatorImpl(state);
    }
}
