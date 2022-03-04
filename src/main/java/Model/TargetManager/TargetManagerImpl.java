package Model.TargetManager;

import Model.Direction;
import Model.Elevator.Elevator;
import Model.ElevatorState.Target;
import Model.ElevatorState.TargetImpl;
import Model.Fabrics.SystemFabric;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TargetManagerImpl implements TargetManager {
    private Elevator[] elevators;
    private TargetList[] specificTargets;
    private TargetList generalTargetsUp;
    private TargetList generalTargetsDown;
    private boolean[] stopped;

    public TargetManagerImpl(Elevator[] elevators) {
        this.elevators = elevators;
        int elevatorsCount = elevators.length;

        specificTargets = new TargetListImpl[elevatorsCount];
        stopped = new boolean[elevatorsCount];
        for(int i = 0; i < elevatorsCount; i ++) {
            specificTargets[i] = new TargetListImpl();
            stopped[i] = false;
        }
        generalTargetsUp = new TargetListImpl();
        generalTargetsDown = new TargetListImpl();
    }

    @Override
    public void achievedTarget(int id) {
        Elevator elevator = elevators[id];
        Direction direction = elevator.getDirection();
        if(direction == Direction.NONE) {
            direction = Direction.UP;
        }

        Target closestTarget = getClosestTarget(elevator, direction);
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

        uncheckTarget(elevator);

    }

    private void uncheckTarget(Elevator elevator) {
        int currentFloor = elevator.getState().getFloor();
        Target newTarget = elevator.getState().getTarget();

        specificTargets[elevator.getState().getID()].remove(currentFloor);

        if(newTarget == null) {
            generalTargetsUp.remove(currentFloor);
            generalTargetsDown.remove(currentFloor);
        }
    }

    private Target getClosestTarget(Elevator elevator, Direction direction) {
        int currentFloor = elevator.getState().getFloor();
        Integer generalTarget = null, specificTarget = null, alternativeTarget = null;
        int id = elevator.getState().getID();

        if(direction == Direction.UP) {
            generalTarget = generalTargetsUp.getClosestAbove(currentFloor);
            alternativeTarget = generalTargetsDown.getClosestAbove(currentFloor);
            specificTarget = specificTargets[id].getClosestAbove(currentFloor);
        }
        else if(direction == Direction.DOWN) {
            generalTarget = generalTargetsDown.getClosestBelow(currentFloor);
            alternativeTarget = generalTargetsUp.getClosestBelow(currentFloor);
            specificTarget = specificTargets[id].getClosestBelow(currentFloor);
        }

        if(generalTarget != null && specificTarget != null) {
            int further = isFurther(elevator, generalTarget, specificTarget);
            if(further < 0) {
                return new TargetImpl(generalTarget, false, direction == Direction.UP, direction == Direction.DOWN);
            }
            else if(further == 0) {
                return new TargetImpl(generalTarget, true, direction == Direction.UP, direction == Direction.DOWN);
            }
            else {
                return new TargetImpl(specificTarget, true, false, false);
            }
        }
        else if(generalTarget == null && specificTarget != null) {
            return new TargetImpl(specificTarget, true, false, false);
        }
        else if(generalTarget != null && specificTarget == null) {
            return new TargetImpl(generalTarget, false, direction == Direction.UP, direction == Direction.DOWN);
        }
        else if(alternativeTarget != null) {
            return new TargetImpl(alternativeTarget, false, direction != Direction.UP, direction != Direction.DOWN);
        }
        else {
            return null;
        }
    }

    private void setNextTarget(Elevator elevator, Target target) {
        int id = elevator.getState().getID();
        int floor = target.getFloor();

        if(target.isSpecific()) {
            specificTargets[id].remove(floor);
        }
        if(target.isUp()) {
            generalTargetsUp.remove(floor);
        }
        if(target.isDown()) {
            generalTargetsDown.remove(floor);
        }

        elevator.setTarget(target);
    }

    @Override
    public void generalRequest(int floor, Direction direction) {
        Optional<Elevator> optionalElevator = Arrays.stream(elevators).
                filter((this::stoppedFilter)).
                filter((elevator) -> directionFilter(elevator, direction, floor)).
                min((eA, eB) -> distanceComparator(eA, eB, floor));

        if(optionalElevator.isEmpty()) {
            switch (direction) {
                case UP:
                    generalTargetsUp.add(floor);
                    break;
                case DOWN:
                    generalTargetsDown.add(floor);
                    break;
            }
        }
        else {
            Elevator elevator = optionalElevator.get();
            swapTargets(elevator, new TargetImpl(floor, false, direction == Direction.UP, direction == Direction.DOWN));
        }
    }

    private boolean stoppedFilter(Elevator elevator) {
        return !stopped[elevator.getState().getID()];
    }

    private static boolean directionFilter(Elevator elevator, Direction direction, int target) {
        Target currentTarget = elevator.getState().getTarget();
        Integer targetFloor = currentTarget == null ? null : currentTarget.getFloor();
        int currentFloor = elevator.getState().getFloor();

        // if elevator is waiting
        if(currentTarget == null) {
            return true;
        }

        if(elevator.getDirection() == direction) {
            if(direction == Direction.UP) {
                if(targetFloor > target && target > currentFloor) {
                    return true;
                }
            }
            else if(direction == Direction.DOWN) {
                if(targetFloor < target && target < currentFloor) {
                    return true;
                }
            }
        }

        return false;
    }

    // < 0 -- second is further than first
    // == 0 -- both within the same distance
    // > 0 -- second is closer than first
    private static int isFurther(Elevator elevator, int firstTarget, int secondTarget) {
        int firstDistance = Math.abs(elevator.getState().getFloor() - firstTarget);
        int secondDistance = Math.abs(elevator.getState().getFloor() - secondTarget);
        return firstDistance - secondDistance;
    }

    private static int distanceComparator(Elevator eA, Elevator eB, int target) {
        if(eA.getState().getTarget() == null && eB.getState().getTarget() != null) {
            return -1;
        }
        if(eA.getState().getTarget() != null && eB.getState().getTarget() == null) {
            return 1;
        }
        int aDistance = Math.abs(eA.getState().getFloor() - target);
        int bDistance = Math.abs(eB.getState().getFloor() - target);
        return aDistance - bDistance;
    }

    @Override
    public void specificRequest(int floor, int elevatorID) {
        Elevator elevator = elevators[elevatorID];
        Target currentTarget = elevator.getState().getTarget();
        Integer targetFloor = currentTarget == null ? null : currentTarget.getFloor();
        int currentFloor = elevator.getState().getFloor();

        if(currentTarget == null) {
            elevator.setTarget(new TargetImpl(floor, true, false, false));
        }
        else if(targetFloor == currentFloor) {
            elevator.setTarget(new TargetImpl(floor, true, false, false));
        }
        // if target <- new <- floor
        // if floor -> new -> target
        else if((targetFloor > floor && floor > currentFloor) ||
                (targetFloor < floor && floor < currentFloor)) {
            swapTargets(elevator, new TargetImpl(floor, true, false, false));
        }
        else {
            specificTargets[elevatorID].add(floor);
        }
    }

    @Override
    public List<Integer> getGeneralTargets(Direction direction) {
        switch (direction) {
            case UP:
                return generalTargetsUp.getAll();
            case DOWN:
                return generalTargetsDown.getAll();
        }
        return null;
    }

    @Override
    public List<Integer> getSpecificTargets(int elevatorId) {
        return specificTargets[elevatorId].getAll();
    }

    private void swapTargets(Elevator elevator, Target newTarget) {
        Target oldTarget = elevator.getState().getTarget();
        int id = elevator.getState().getID();

        elevator.setTarget(newTarget);
        restoreTarget(id, oldTarget);
    }

    private void restoreTarget(int elevatorId, Target oldTarget) {
        if(oldTarget == null) {
            return;
        }
        int targetFloor = oldTarget.getFloor();

        if(oldTarget.isSpecific()) {
            specificTargets[elevatorId].add(targetFloor);
        }
        if(oldTarget.isUp()) {
            generalTargetsUp.add(targetFloor);
        }
        if(oldTarget.isDown()) {
            generalTargetsDown.add(targetFloor);
        }
    }

    public void changeElevatorStop(int index) {
        stopped[index] = !stopped[index];
        if(stopped[index]) {
            swapTargets(elevators[index], null);
        }
        else {
            achievedTarget(index);
        }
    }
}
