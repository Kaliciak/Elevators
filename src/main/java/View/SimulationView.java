package View;

import Model.Direction;
import Model.ElevatorState.ElevatorState;
import Model.Fabrics.SystemFabric;
import ViewModel.SimulationViewModel;
import ViewModel.SimulationViewModelImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static javafx.scene.text.Font.font;

public class SimulationView {
    private SimulationViewModel viewModel;

    private int floorsCount = 8;
    private int elevatorsCount = 5;
    private IndexedCanvas[][] elevatorCanvas = new IndexedCanvas[elevatorsCount][floorsCount];

    @FXML
    private VBox floorsVBox;

    @FXML
    private HBox elevatorHBox;

    @FXML
    void initialize() {
        // TODO: inject class
        viewModel = new SimulationViewModelImpl();

        fillFloorsPane();
        fillElevatorsPane();
        redraw();
    }

    @FXML
    void simulationStep(ActionEvent event) {
        viewModel.step();
        redraw();
    }

    private void requestUp(ActionEvent event) {
        IndexedButton button = (IndexedButton) event.getTarget();
        viewModel.pressedFloorButton(button.id, Direction.UP);
    }

    private void requestDown(ActionEvent event) {
        IndexedButton button = (IndexedButton) event.getTarget();
        viewModel.pressedFloorButton(button.id, Direction.DOWN);
    }

    private void fillFloorsPane() {
        for(int i = 0; i < floorsCount; i ++) {
            HBox floorBox = new HBox();

            Text floorText = new Text(Integer.toString(5 - i));
            floorText.setFont(font(floorText.getFont().getFamily(), 12));
            HBox.setHgrow(floorText, Priority.ALWAYS);
            floorBox.getChildren().add(floorText);

            IndexedButton upButton = new IndexedButton("UP", i);
            upButton.setOnAction(this::requestUp);
            HBox.setHgrow(upButton, Priority.ALWAYS);
            floorBox.getChildren().add(upButton);

            IndexedButton downButton = new IndexedButton("DOWN", i);
            downButton.setOnAction(this::requestDown);
            HBox.setHgrow(downButton, Priority.ALWAYS);
            floorBox.getChildren().add(downButton);

            floorsVBox.getChildren().add(floorBox);
        }
    }

    private void specificRequest(MouseEvent event) {
        IndexedCanvas canvas = (IndexedCanvas) event.getTarget();
        viewModel.pressedElevatorCanvas(canvas.elevator, canvas.floor);
    }

    private void fillElevatorsPane() {
        for(int elevator = 0; elevator < elevatorsCount; elevator ++) {
            VBox newBox = new VBox();

            for(int floor = 0; floor < floorsCount; floor ++) {
                elevatorCanvas[elevator][floor] = new IndexedCanvas(40, 55, elevator, floor);
                elevatorCanvas[elevator][floor].setOnMouseClicked(this::specificRequest);
                VBox.setVgrow(elevatorCanvas[elevator][floor], Priority.ALWAYS);
                newBox.getChildren().add(elevatorCanvas[elevator][floor]);
            }

            HBox.setHgrow(newBox, Priority.ALWAYS);
            elevatorHBox.getChildren().add(newBox);
        }
    }

    private void redraw() {
        ElevatorState[] elevatorStates = viewModel.getElevatorStates();

        for(int elevator = 0; elevator < elevatorsCount; elevator ++) {
            for(int floor = 0; floor < floorsCount; floor ++) {
                GraphicsContext gc = elevatorCanvas[elevator][floor].getGraphicsContext2D();
                double height = elevatorCanvas[elevator][floor].getHeight();
                double width = elevatorCanvas[elevator][floor].getWidth();
                gc.setFill(Color.ORANGE);
                gc.fillRect(0,0, width - 2, height - 2);
            }
        }

        for(int elevator = 0; elevator < elevatorsCount; elevator ++) {
            int floorIndex = getFloorIndex(elevatorStates[elevator].getFloor());
            GraphicsContext gc = elevatorCanvas[elevator][floorIndex].getGraphicsContext2D();
            gc.setFill(Color.BLACK);
            gc.fillOval(10, 10, 10, 10);
        }
    }

    //TODO: Move to viewModel
    private int getFloorIndex(int floor) {
        return 5 - floor;
    }
}
