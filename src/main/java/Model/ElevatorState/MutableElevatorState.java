package Model.ElevatorState;

public interface MutableElevatorState extends ElevatorState {
    void setFloor(int floor);
    void setTarget(Target target);
    void setDoorOpen(boolean open);
}
