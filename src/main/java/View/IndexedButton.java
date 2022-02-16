package View;

import javafx.scene.control.Button;

public class IndexedButton extends Button {
    public int id;

    public IndexedButton(String text, int id) {
        super(text);
        this.id = id;
    }
}
