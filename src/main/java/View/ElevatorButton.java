package View;

import javafx.scene.control.Button;

public class ElevatorButton extends Button {
    public int elevator, floor;

    public ElevatorButton(int elevator, int floor) {
        super();
        setStyleClass("elevator_shaft");
        this.elevator = elevator;
        this.floor = floor;
    }

    public void setStyleClass(String style) {
        getStyleClass().clear();
        getStyleClass().add(style);
    }
}
