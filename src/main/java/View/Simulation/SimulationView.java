package View.Simulation;

import Model.Direction;
import Model.ElevatorState.ElevatorState;
import Model.ElevatorState.Target;
import View.IndexedButton;
import View.ElevatorButton;
import View.Menu.MenuView;
import View.StopButton;
import ViewModel.Simulation.SimulationViewModel;
import ViewModel.Simulation.SimulationViewModelImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Stop;
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
    private ElevatorButton[][] elevatorButtons;

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
    private HBox stopHBox;
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
        elevatorButtons = new ElevatorButton[elevatorsCount][floorsCount];

        viewModel = new SimulationViewModelImpl(elevatorsCount, maxFloor, floorsCount);

        synchroniseScrollPanes(floorsScrollPane, elevatorsScrollPane);
        floorsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        floorsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        fillFloorsPane();
        fillElevatorsPane();
        fillStopHBox();
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
        ElevatorButton elevatorButton = (ElevatorButton) event.getTarget();
        viewModel.pressedElevatorCanvas(elevatorButton.elevator, elevatorButton.floor);
        redraw();
    }

    private void fillFloorsPane() {
        for(int i = 0; i < floorsCount; i ++) {
            Pane pane = new Pane();
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
                elevatorButtons[elevator][floor] = new ElevatorButton(elevator, floor);
                elevatorButtons[elevator][floor].setOnMouseClicked(this::specificRequest);
                VBox.setVgrow(elevatorButtons[elevator][floor], Priority.ALWAYS);
                newBox.getChildren().add(elevatorButtons[elevator][floor]);
            }

            HBox.setHgrow(newBox, Priority.ALWAYS);
            elevatorHBox.getChildren().add(newBox);
        }
    }

    private void redraw() {
        drawShafts();
        drawRequests();
        drawTargets();
        drawElevators();

        mainPane.requestFocus();
    }

    private void drawShafts() {
        for(int elevator = 0; elevator < elevatorsCount; elevator ++) {
            for(int floor = 0; floor < floorsCount; floor ++) {
                elevatorButtons[elevator][floor].setStyleClass("elevator_shaft");
            }
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
        for(ElevatorButton[] elevatorArr: elevatorButtons) {
            List<Integer> elevatorRequests = viewModel.getSpecificTargets(elevatorArr[0].elevator);
            Arrays.stream(elevatorArr).forEach((canvas) -> paintElevatorButton(canvas, elevatorRequests));
        }
    }

    private void drawTargets() {
        for(ElevatorState state: viewModel.getElevatorStates()) {
            int id = state.getID();
            Target target = state.getTarget();
            if(target == null) {
                continue;
            }

            elevatorButtons[id][target.getFloor()].setStyleClass("elevator_shaft_target");
        }
    }

    private void drawElevators() {
        ElevatorState[] elevatorStates = viewModel.getElevatorStates();

        for(int elevator = 0; elevator < elevatorsCount; elevator ++) {
            int floorIndex = elevatorStates[elevator].getFloor();
            ElevatorButton elevatorButton = elevatorButtons[elevator][floorIndex];
            if(elevatorStates[elevator].isDoorOpen()) {
                elevatorButton.setStyleClass("elevator_open");
            }
            else {
                elevatorButton.setStyleClass("elevator_closed");
            }
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

    private void paintElevatorButton(ElevatorButton elevatorButton, List<Integer> requestList) {
        if(requestList.contains(elevatorButton.floor)) {
            elevatorButton.setStyleClass("elevator_shaft_request");
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

    void fillStopHBox() {
        for(int i = 0; i < elevatorsCount; i ++) {
            Button button = new StopButton(i + "", i);
            button.getStyleClass().add("button_");
            button.setOnMouseClicked(this::pressedStopButton);
            HBox.setHgrow(button, Priority.ALWAYS);
            stopHBox.getChildren().add(button);
        }
    }

    void pressedStopButton(MouseEvent event) {
        StopButton button = (StopButton) event.getTarget();
        viewModel.pressedElevatorStop(button.index);
        redraw();
    }
}
