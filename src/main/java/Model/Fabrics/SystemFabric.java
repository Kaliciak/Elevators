package Model.Fabrics;

import Model.ElevatorSystem.ElevatorSystem;

public interface SystemFabric {
    ElevatorSystem createElevatorSystem(int elevatorsCount);
}
