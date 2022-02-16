package View;

import Model.Direction;
import javafx.scene.control.Button;

public class IndexedButton extends Button {
    public int id;
    public Direction direction;

    public IndexedButton(int id, Direction direction) {
        super();
        this.id = id;
        this.direction = direction;
    }
}
