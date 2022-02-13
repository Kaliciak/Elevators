package Model.ElevatorState;

public interface MutableElevatorState extends ElevatorState {
    void setFloor(int floor);
    void setTarget(Integer floor);
    void setDoorOpen(boolean open);
}
