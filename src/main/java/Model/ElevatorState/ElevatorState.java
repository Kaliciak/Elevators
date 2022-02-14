package Model.ElevatorState;

public interface ElevatorState {
    int getID();
    int getFloor();
    Target getTarget();
    boolean isDoorOpen();
}
