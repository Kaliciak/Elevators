package Model.ElevatorState;

public class TargetImpl implements Target {
    private int floor;
    private boolean specific, up, down;

    public TargetImpl(int floor, boolean specific, boolean up, boolean down) {
        this.floor = floor;
        this.specific = specific;
        this.up = up;
        this.down = down;
    }

    @Override
    public int getFloor() {
        return floor;
    }

    @Override
    public boolean isSpecific() {
        return specific;
    }

    @Override
    public boolean isUp() {
        return up;
    }

    @Override
    public boolean isDown() {
        return down;
    }
}
