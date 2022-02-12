package Model;

public class ElevatorStateImpl implements ElevatorState {
    private int id;
    private int floor;
    private int target;

    public ElevatorStateImpl(int id, int floor, int target) {
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
    public int getTarget() {
        return target;
    }
}
