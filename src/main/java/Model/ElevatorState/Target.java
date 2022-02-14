package Model.ElevatorState;

public interface Target {
    int getFloor();
    boolean isSpecific();
    boolean isUp();
    boolean isDown();
}
