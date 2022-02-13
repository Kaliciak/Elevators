package Model.ElevatorState;

public class ElevatorStateImpl implements MutableElevatorState {
    private int id;
    private int floor;
    private Integer target;
    private boolean openDoors = false;

    public ElevatorStateImpl(int id, Integer floor, Integer target) {
        this.id = id;
        this.floor = floor;
        this.target = target;
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
    public Integer getTarget() {
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
    public void setTarget(Integer target) {
        this.target = target;
    }

    @Override
    public void setDoorOpen(boolean open) {
        this.openDoors = open;
    }
}
