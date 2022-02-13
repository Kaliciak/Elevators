package Model.TargetManager;

public interface TargetList {
    void add(int floor);
    void remove(int floor);
    Integer getClosestAbove(int floor);
    Integer getClosestBelow(int floor);
}
