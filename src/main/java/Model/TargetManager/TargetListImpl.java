package Model.TargetManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TargetListImpl implements TargetList {
    private List<Integer> list = new ArrayList<>();

    @Override
    public void add(int floor) {
        if(!list.contains(floor)) {
            list.add(floor);
        }
    }

    @Override
    public void remove(int floor) {
        list.remove(floor);
    }

    @Override
    public Integer getClosestAbove(int floor) {
        Optional<Integer> found = list.stream().filter((x) -> (x > floor)).min(Comparator.comparingInt(a -> a));
        if(found.isEmpty()) {
            return null;
        }
        return found.get();
    }

    @Override
    public Integer getClosestBelow(int floor) {
        Optional<Integer> found = list.stream().filter((x) -> (x < floor)).max(Comparator.comparingInt(a -> a));
        if(found.isEmpty()) {
            return null;
        }
        return found.get();
    }
}
