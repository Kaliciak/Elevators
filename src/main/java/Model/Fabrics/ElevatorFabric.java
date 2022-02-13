package Model.Fabrics;

import Model.Elevator.Elevator;

public interface ElevatorFabric {
    Elevator createElevator(int id);
}
