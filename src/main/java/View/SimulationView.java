package View;

import Model.ElevatorState;
import ViewModel.SimulationViewModel;
import ViewModel.SimulationViewModelImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SimulationView {
    private SimulationViewModel viewModel;

    private int floorsCount = 8;
    private int elevatorsCount = 5;
    private Canvas[][] elevatorCanvas = new Canvas[elevatorsCount][floorsCount];

    @FXML
    private HBox elevatorHBox;

    @FXML
    void initialize() {
        // TODO: inject class
        viewModel = new SimulationViewModelImpl();

        for(int elevator = 0; elevator < elevatorsCount; elevator ++) {
            VBox newBox = new VBox();

            for(int floor = 0; floor < floorsCount; floor ++) {
                elevatorCanvas[elevator][floor] = new Canvas(100, 50);
                VBox.setVgrow(elevatorCanvas[elevator][floor], Priority.ALWAYS);
                newBox.getChildren().add(elevatorCanvas[elevator][floor]);
            }

            HBox.setHgrow(newBox, Priority.ALWAYS);
            elevatorHBox.getChildren().add(newBox);
        }

        redraw();
    }

    @FXML
    void simulationStep(ActionEvent event) {
        redraw();
        System.out.println("PRESSSS");
    }

    private void redraw() {
        ElevatorState[] elevatorStates = viewModel.getElevatorStates();

        for(int elevator = 0; elevator < elevatorsCount; elevator ++) {
            for(int floor = 0; floor < floorsCount; floor ++) {
                double height = elevatorCanvas[elevator][floor].getHeight();
                double width = elevatorCanvas[elevator][floor].getWidth();
                elevatorCanvas[elevator][floor].getGraphicsContext2D().clearRect(0,0, width, height);
            }
        }

        for(int elevator = 0; elevator < elevatorsCount; elevator ++) {
            int floorIndex = getFloorIndex(elevatorStates[elevator].getFloor());
            GraphicsContext gc = elevatorCanvas[elevator][floorIndex].getGraphicsContext2D();
            gc.setFill(Color.BLACK);
            gc.fillOval(10, 10, 10, 10);
        }
    }

    private int getFloorIndex(int floor) {
        // 5 -> 0
        // -2 -> 7
        // x -> 5 - x
        return 5 - floor;
    }
}
