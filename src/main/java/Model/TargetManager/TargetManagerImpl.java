package Model.TargetManager;

import Model.Direction;
import Model.Elevator.Elevator;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;

public class TargetManagerImpl implements TargetManager {
    private Elevator[] elevators;
    private boolean[] toSpecificTarget;
    private TargetList[] specificTargets;
    private TargetList generalTargetsUp;
    private TargetList generalTargetsDown;

    public TargetManagerImpl(Elevator[] elevators) {
        this.elevators = elevators;
        int elevatorsCount = elevators.length;

        //TODO: use fabric
        toSpecificTarget = new boolean[elevatorsCount];
        specificTargets = new TargetListImpl[elevatorsCount];
        for(int i = 0; i < elevatorsCount; i ++) {
            specificTargets[i] = new TargetListImpl();
        }
        generalTargetsUp = new TargetListImpl();
        generalTargetsDown = new TargetListImpl();
    }

    @Override
    public void achievedTarget(int id) {
        Elevator elevator = elevators[id];
        Direction direction = elevator.getDirection();

        Integer closestTarget = getClosestTarget(elevator, direction);
        if(closestTarget != null) {
            setNextTarget(elevator, closestTarget);
        }
        // if no targets in that direction
        else {
            closestTarget = getClosestTarget(elevator, direction.oppositeDirection());

            if(closestTarget != null) {
                setNextTarget(elevator, closestTarget);
            }
            else {
                elevator.setTarget(null);
            }
        }

    }

    private Integer getClosestTarget(Elevator elevator, Direction direction) {
        int currentFloor = elevator.getState().getFloor();
        Integer generalTarget = null, specificTarget = null;
        int id = elevator.getState().getID();

        if(direction == Direction.UP) {
            generalTarget = generalTargetsUp.getClosestAbove(currentFloor);
            specificTarget = specificTargets[id].getClosestAbove(currentFloor);
        }
        else if(direction == Direction.DOWN) {
            generalTarget = generalTargetsDown.getClosestBelow(currentFloor);
            specificTarget = specificTargets[id].getClosestBelow(currentFloor);
        }

        if(generalTarget != null && specificTarget != null) {
            if(isFurther(elevator, generalTarget, specificTarget)) {
                return specificTarget;
            }
            else {
                return generalTarget;
            }
        }
        else if(generalTarget == null && specificTarget != null) {
            return specificTarget;
        }
        else if(generalTarget != null && specificTarget == null) {
            return generalTarget;
        }
        else {
            return null;
        }
    }

    private void setNextTarget(Elevator elevator, int target) {
        int currentFloor = elevator.getState().getFloor();
        int id = elevator.getState().getID();

        toSpecificTarget[id] = (specificTargets[id].getClosestAbove(currentFloor) == target ||
                specificTargets[id].getClosestBelow(currentFloor) == target);

        specificTargets[id].remove(target);
        generalTargetsUp.remove(target);
        generalTargetsDown.remove(target);

        elevator.setTarget(target);
    }

    @Override
    public void generalRequest(int floor, Direction direction) {
        Optional<Elevator> optionalElevator = Arrays.stream(elevators).
                filter((elevator) -> directionFilter(elevator, direction, floor)).
                min((eA, eB) -> distanceComparator(eA, eB, floor));

        if(optionalElevator.isEmpty()) {
            switch (direction) {
                case UP:
                    generalTargetsUp.add(floor);
                case DOWN:
                    generalTargetsDown.add(floor);
            }
        }
        else {
            Elevator elevator = optionalElevator.get();
            swapTargets(elevator, floor, false);
        }
    }

    private static boolean directionFilter(Elevator elevator, Direction direction, int target) {
        Integer currentTarget = elevator.getState().getTarget();
        int currentFloor = elevator.getState().getFloor();

        // if elevator is waiting
        if(currentTarget == null) {
            return true;
        }

        if(elevator.getDirection() == direction) {
            if(direction == Direction.UP) {
                if(currentTarget > target && target > currentFloor) {
                    return true;
                }
            }
            else if(direction == Direction.DOWN) {
                if(currentTarget < target && target < currentFloor) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isFurther(Elevator elevator, int firstTarget, int secondTarget) {
        int firstDistance = Math.abs(elevator.getState().getFloor() - firstTarget);
        int secondDistance = Math.abs(elevator.getState().getFloor() - secondTarget);
        return firstDistance > secondDistance;
    }

    private static int distanceComparator(Elevator eA, Elevator eB, int target) {
        if(eA.getState().getTarget() == null) {
            return -1;
        }
        if(eB.getState().getTarget() == null) {
            return 1;
        }
        int aDistance = Math.abs(eA.getState().getFloor() - target);
        int bDistance = Math.abs(eB.getState().getFloor() - target);
        return aDistance - bDistance;
    }

    @Override
    public void specificRequest(int floor, int elevatorID) {
        Elevator elevator = elevators[elevatorID];
        Integer currentTarget = elevator.getState().getTarget();
        int currentFloor = elevator.getState().getFloor();

        if(currentTarget == null) {
            elevator.setTarget(floor);
        }
        else if(currentTarget == currentFloor) {
            return;
        }
        // if target <- new <- floor
        // if floor -> new -> target
        else if((currentTarget > floor && floor > currentFloor) ||
                (currentTarget < floor && floor < currentFloor)) {
            swapTargets(elevator, floor, true);
        }
        else {
            specificTargets[elevatorID].add(floor);
        }
    }

    private void swapTargets(Elevator elevator, int newTarget, boolean isSpecific) {
        int oldTarget = elevator.getState().getTarget();
        int id = elevator.getState().getID();
        int currentFloor = elevator.getState().getFloor();
        elevator.setTarget(newTarget);
        if(toSpecificTarget[id]) {
            specificTargets[id].add(oldTarget);
        }
        else {
            if(oldTarget > currentFloor) {
                generalTargetsUp.add(oldTarget);
            }
            else {
                generalTargetsDown.add(oldTarget);
            }
        }
        toSpecificTarget[id] = isSpecific;
    }
}
