package Model.ElevatorState;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TargetImplTest {
    @Test
    void gettersTest() {
        int floor = 4;
        boolean isSpecific = false;
        boolean isUp = true;
        boolean isDown = false;
        Target target = new TargetImpl(floor, isSpecific, isUp, isDown);

        assertEquals(floor, target.getFloor());
        assertEquals(isSpecific, target.isSpecific());
        assertEquals(isUp, target.isUp());
        assertEquals(isDown, target.isDown());
    }
}