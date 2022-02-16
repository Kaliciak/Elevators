package View;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class IndexedCanvas extends Canvas {
    public int elevator, floor;

    public IndexedCanvas(double width, double height, int elevator, int floor) {
        super(width, height);
        this.elevator = elevator;
        this.floor = floor;
    }

    public void fill(Paint paint) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(paint);
        gc.fillRect(0,0, getWidth() - 2, getHeight() - 2);
    }
}
