package Model.ElevatorState;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ElevatorStateImplTest {
    @Test
    void getterTest() {
        int id = 0;
        int floor = 1;
        Target target = mock(Target.class);
        boolean openDoors = true;

        ElevatorState state1 = new ElevatorStateImpl(id, floor, target, openDoors);
        ElevatorState state2 = new ElevatorStateImpl(id, floor, target);

        assertEquals(id, state1.getID());
        assertEquals(floor, state1.getFloor());
        assertEquals(target, state1.getTarget());
        assertEquals(openDoors, state1.isDoorOpen());
        assertEquals(id, state2.getID());
        assertEquals(floor, state2.getFloor());
        assertEquals(target, state2.getTarget());
        assertFalse(state2.isDoorOpen());
    }

    @Test
    void setterTest() {
        int floor = 4;
        Target target = mock(Target.class);
        boolean openDoors = true;
        MutableElevatorState state = new ElevatorStateImpl(0, 1, null);

        state.setFloor(floor);
        state.setTarget(target);
        state.setDoorOpen(openDoors);

        assertEquals(0, state.getID());
        assertEquals(floor, state.getFloor());
        assertEquals(target, state.getTarget());
        assertEquals(openDoors, state.isDoorOpen());
    }
}