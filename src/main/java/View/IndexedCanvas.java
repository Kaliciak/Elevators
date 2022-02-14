package View;

import javafx.scene.canvas.Canvas;

public class IndexedCanvas extends Canvas {
    int elevator, floor;

    public IndexedCanvas(double width, double height, int elevator, int floor) {
        super(width, height);
        this.elevator = elevator;
        this.floor = floor;
    }
}
