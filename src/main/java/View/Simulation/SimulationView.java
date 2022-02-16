package View.Simulation;

import Model.Direction;
import Model.ElevatorState.ElevatorState;
import Model.ElevatorState.Target;
import View.IndexedButton;
import View.IndexedCanvas;
import View.Menu.MenuView;
import ViewModel.Simulation.SimulationViewModel;
import ViewModel.Simulation.SimulationViewModelImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;
import java.util.List;

import static javafx.scene.text.Font.font;

public class SimulationView {
    private SimulationViewModel viewModel;

    private int maxFloor;
    private int floorsCount;
    private int elevatorsCount;
    private IndexedButton[] buttonsUp;
    private IndexedButton[] buttonsDown;
    private IndexedCanvas[][] elevatorCanvas;

    private final int elevatorHeight = 55;
    private final int floorWidth = 99;
    private final int fontSize = 12;
    private final int floorTextWidth = 31;

    @FXML
    private AnchorPane mainPane;
    @FXML
    private VBox floorsVBox;
    @FXML
    private HBox elevatorHBox;
    @FXML
    private ScrollPane floorsScrollPane;
    @FXML
    private ScrollPane elevatorsScrollPane;

    public void initialize(int elevatorsCount, int maxFloor, int minFloor) {
        this.elevatorsCount = elevatorsCount;
        this.maxFloor = maxFloor;
        this.floorsCount = maxFloor - minFloor + 1;

        buttonsUp = new IndexedButton[floorsCount];
        buttonsDown = new IndexedButton[floorsCount];
        elevatorCanvas = new IndexedCanvas[elevatorsCount][floorsCount];

        // TODO: inject class
        viewModel = new SimulationViewModelImpl(elevatorsCount, maxFloor, floorsCount);

        synchroniseScrollPanes(floorsScrollPane, elevatorsScrollPane);
        floorsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        floorsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

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
            Pane pane = new Pane();
            pane.setMinHeight(elevatorHeight);
            pane.setMinWidth(floorWidth);
            pane.getStyleClass().add("floor_pane");
            floorsVBox.getChildren().add(pane);

            HBox textBox = new HBox();
            textBox.setAlignment(Pos.CENTER_LEFT);
            textBox.setMinWidth(floorWidth);
            textBox.setMinHeight(elevatorHeight);
            textBox.layoutYProperty().setValue(0);
            textBox.layoutXProperty().setValue(5);
            pane.getChildren().add(textBox);

            Text floorText = new Text(Integer.toString(maxFloor - i));
            floorText.textAlignmentProperty().setValue(TextAlignment.CENTER);
            floorText.setWrappingWidth(floorTextWidth);
            floorText.setFont(font(floorText.getFont().getFamily(), fontSize));
            floorText.getStyleClass().add("floor_pane_text");
            HBox.setHgrow(floorText, Priority.ALWAYS);
            textBox.getChildren().add(floorText);

            IndexedButton upButton = new IndexedButton(i, Direction.UP);
            upButton.setOnAction(this::requestUp);
            upButton.getStyleClass().add("floor_button_up");
            upButton.layoutXProperty().setValue(42);
            upButton.layoutYProperty().setValue(6);
            pane.getChildren().add(upButton);

            IndexedButton downButton = new IndexedButton(i, Direction.DOWN);
            downButton.setOnAction(this::requestDown);
            downButton.getStyleClass().add("floor_button_down");
            downButton.layoutXProperty().setValue(42);
            downButton.layoutYProperty().setValue(26);
            pane.getChildren().add(downButton);

            buttonsUp[i] = upButton;
            buttonsDown[i] = downButton;
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
        drawTargets();

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

    private void drawTargets() {
        for(ElevatorState state: viewModel.getElevatorStates()) {
            int id = state.getID();
            Target target = state.getTarget();
            if(target == null) {
                continue;
            }

            elevatorCanvas[id][target.getFloor()].fill(Color.RED);
        }
    }

    private void paintButton(IndexedButton button, List<Integer> requestList) {
        button.getStyleClass().clear();
        Direction direction = button.direction;
        if(requestList.contains(button.id)) {
            if(direction == Direction.UP) {
                button.getStyleClass().add("floor_button_up_hold");
            }
            else if(direction == Direction.DOWN) {
                button.getStyleClass().add("floor_button_down_hold");
            }
        }
        else {
            if(direction == Direction.UP) {
                button.getStyleClass().add("floor_button_up");
            }
            else if(direction == Direction.DOWN) {
                button.getStyleClass().add("floor_button_down");
            }
        }
    }

    private void paintCanvas(IndexedCanvas canvas, List<Integer> requestList) {
        if(requestList.contains(canvas.floor)) {
            canvas.fill(Color.ORANGE);
        }
        else {
            canvas.fill(Color.LIGHTGREEN);
        }
    }

    private void synchroniseScrollPanes(ScrollPane first, ScrollPane second) {
        first.vvalueProperty().addListener((observable, oldValue, newValue) -> second.setVvalue((Double) newValue));
        second.vvalueProperty().addListener((observable, oldValue, newValue) -> first.setVvalue((Double) newValue));
    }

    @FXML
    void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/fxml/Menu.fxml"));
            Parent root = loader.load();
            MenuView menuView = loader.getController();
            menuView.initialize(elevatorsCount, maxFloor, maxFloor - floorsCount + 1);

            mainPane.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
