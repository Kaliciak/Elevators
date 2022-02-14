package View;

import Model.Direction;
import Model.Elevator.Elevator;
import Model.ElevatorState.ElevatorState;
import ViewModel.SimulationViewModel;
import ViewModel.SimulationViewModelImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

import static javafx.scene.text.Font.font;

public class SimulationView {
    private SimulationViewModel viewModel;

    private int floorsCount = 8;
    private int elevatorsCount = 5;
    private IndexedButton[] buttonsUp = new IndexedButton[floorsCount];
    private IndexedButton[] buttonsDown = new IndexedButton[floorsCount];
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
        redraw();
    }

    private void requestDown(ActionEvent event) {
        IndexedButton button = (IndexedButton) event.getTarget();
        viewModel.pressedFloorButton(button.id, Direction.DOWN);
        redraw();
    }

    private void specificRequest(MouseEvent event) {
        IndexedCanvas canvas = (IndexedCanvas) event.getTarget();
        viewModel.pressedElevatorCanvas(canvas.elevator, canvas.floor);
        redraw();
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

            buttonsUp[i] = upButton;
            buttonsDown[i] = downButton;

            floorsVBox.getChildren().add(floorBox);
        }
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

        drawRequests();

        for(int elevator = 0; elevator < elevatorsCount; elevator ++) {
            int floorIndex = elevatorStates[elevator].getFloor();
            GraphicsContext gc = elevatorCanvas[elevator][floorIndex].getGraphicsContext2D();
            gc.setFill(Color.BLACK);
            gc.fillOval(10, 10, 10, 10);
        }
    }

    private void drawRequests() {
        // general
        // up
        List<Integer> upRequests = viewModel.getGeneralTargets(Direction.UP);
        Arrays.stream(buttonsUp).forEach((button) -> paintButton(button, upRequests));
        // down
        List<Integer> downRequests = viewModel.getGeneralTargets(Direction.DOWN);
        Arrays.stream(buttonsDown).forEach((button) -> paintButton(button, downRequests));

        // specific
        for(IndexedCanvas[] elevatorArr: elevatorCanvas) {
            List<Integer> elevatorRequests = viewModel.getSpecificTargets(elevatorArr[0].elevator);
            Arrays.stream(elevatorArr).forEach((canvas) -> paintCanvas(canvas, elevatorRequests));
        }

    }

    private void paintButton(IndexedButton button, List<Integer> requestList) {
        if(requestList.contains(button.id)) {
            button.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
        }
        else {
            button.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
        }
    }

    private void paintCanvas(IndexedCanvas canvas, List<Integer> requestList) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double height = canvas.getHeight();
        double width = canvas.getWidth();
        if(requestList.contains(canvas.floor)) {
            gc.setFill(Color.ORANGE);
        }
        else {
            gc.setFill(Color.LIGHTGREEN);
        }
        gc.fillRect(0,0, width - 2, height - 2);
    }
}
