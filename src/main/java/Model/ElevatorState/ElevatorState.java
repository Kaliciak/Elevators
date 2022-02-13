package Model.ElevatorState;

public interface ElevatorState {
    int getID();
    int getFloor();
    Integer getTarget();
    boolean isDoorOpen();
}
