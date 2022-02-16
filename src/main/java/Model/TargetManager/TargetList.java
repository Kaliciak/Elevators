package Model.TargetManager;

import java.util.List;

public interface TargetList {
    void add(int floor);
    void remove(int floor);
    Integer getClosestAbove(int floor);
    Integer getClosestBelow(int floor);
    List<Integer> getAll();
}
