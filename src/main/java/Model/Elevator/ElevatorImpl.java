package Model.Elevator;

import Model.Direction;
import Model.ElevatorState.ElevatorState;
import Model.ElevatorState.MutableElevatorState;
import Model.TargetManager.TargetManagerDelegate;

public class ElevatorImpl implements Elevator {
    private TargetManagerDelegate delegate;
    private MutableElevatorState state;
    private Direction direction = Direction.NONE;

    public ElevatorImpl(MutableElevatorState state) {
        this.state = state;
    }

    @Override
    public ElevatorState getState() {
        return state;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setTarget(Integer floor) {
        state.setTarget(floor);
    }

    @Override
    public void step() {
        if(state.isDoorOpen()) {
            state.setDoorOpen(false);
            delegate.achievedTarget(state.getID());
        }
        else {
            // if no target
            if(state.getTarget() == null) {
                direction = Direction.NONE;
            }

            // if at target
            else if(state.getTarget() == state.getFloor()) {
                state.setDoorOpen(true);
            }

            // if target is above
            else if(state.getTarget() > state.getFloor()) {
                state.setFloor(state.getFloor() + 1);
                direction = Direction.UP;
            }

            // if target is below
            else if(state.getTarget() < state.getFloor()) {
                state.setFloor(state.getFloor() - 1);
                direction = Direction.DOWN;
            }
        }
    }

    @Override
    public void setDelegate(TargetManagerDelegate delegate) {
        this.delegate = delegate;
    }
}
