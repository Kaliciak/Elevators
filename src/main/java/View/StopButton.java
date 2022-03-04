package View;


import javafx.scene.control.Button;

public class StopButton extends Button {
    public int index;

    public StopButton(String text, int index) {
        super(text);
        this.index = index;
    }
}
