package Model;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.stream.IntStream;

public class ElevatorSystemImpl implements ElevatorSystem {
    private int elevatorsCount = 5;
    private Elevator[] elevators = new Elevator[elevatorsCount];

    @Override
    public ElevatorState[] getElevatorStates() {
        // TODO: delete random states
        ElevatorState[] newStates = new ElevatorState[elevatorsCount];

        Random rand = new Random();
        PrimitiveIterator.OfInt iterator = rand.ints(-2, 6).iterator();
        for(int i = 0; i < elevatorsCount; i ++) {
            newStates[i] = new ElevatorStateImpl(iterator.nextInt(), iterator.nextInt(), iterator.nextInt());
            System.out.println("Created state: " + newStates[i].getID() + " " + newStates[i].getFloor() + " " + newStates[i].getTarget());
        }

        return newStates;
    }
}
