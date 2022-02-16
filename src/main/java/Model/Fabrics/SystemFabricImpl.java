package Model.Fabrics;

import Model.Elevator.Elevator;
import Model.ElevatorSystem.ElevatorSystem;
import Model.ElevatorSystem.ElevatorSystemImpl;
import Model.TargetManager.TargetManager;
import Model.TargetManager.TargetManagerImpl;

import java.util.ArrayList;

public class SystemFabricImpl implements SystemFabric {
    @Override
    public ElevatorSystem createElevatorSystem(int elevatorsCount) {
        ArrayList<Elevator> elevators = new ArrayList<>();
        ElevatorFabric elevatorFabric = new ElevatorFabricImpl();

        for(int i = 0; i < elevatorsCount; i ++) {
            elevators.add(elevatorFabric.createElevator(i));
        }

        TargetManager targetManager = new TargetManagerImpl(elevators.toArray(Elevator[]::new));

        for(Elevator elevator: elevators) {
            elevator.setDelegate(targetManager);
        }

        return new ElevatorSystemImpl(elevatorsCount, elevators.toArray(Elevator[]::new), targetManager);
    }
}
