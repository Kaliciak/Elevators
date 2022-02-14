package View;

import javafx.scene.control.Button;

public class IndexedButton extends Button {
    int id;

    IndexedButton(String text, int id) {
        super(text);
        this.id = id;
    }
}