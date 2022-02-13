package Model;

public enum Direction {
    UP {
        public Direction oppositeDirection() {
            return DOWN;
        }
    },
    DOWN {
        public Direction oppositeDirection() {
            return UP;
        }
    },
    NONE {
        public Direction oppositeDirection() {
            return NONE;
        }
    };

    public abstract Direction oppositeDirection();
}
