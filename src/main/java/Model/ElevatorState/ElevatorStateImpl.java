package Model.ElevatorState;

public class ElevatorStateImpl implements MutableElevatorState {
    private int id;
    private int floor;
    private Target target;
    private boolean openDoors;

    public ElevatorStateImpl(int id, Integer floor, Target target, boolean openDoors) {
        this.id = id;
        this.floor = floor;
        this.target = target;
        this.openDoors = openDoors;
    }

    public ElevatorStateImpl(int id, Integer floor, Target target) {
        this(id, floor, target, false);
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getFloor() {
        return floor;
    }

    @Override
    public Target getTarget() {
        return target;
    }

    @Override
    public boolean isDoorOpen() {
        return openDoors;
    }

    @Override
    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public void setTarget(Target target) {
        this.target = target;
    }

    @Override
    public void setDoorOpen(boolean open) {
        this.openDoors = open;
    }
}
