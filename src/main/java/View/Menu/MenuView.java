package View.Menu;

import View.Simulation.SimulationView;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuView {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private Slider elevatorCountSlider;
    @FXML
    private Text elevatorCountText;
    @FXML
    private Slider maxFloorSlider;
    @FXML
    private Slider minFloorSlider;
    @FXML
    private Text maxFloorText;
    @FXML
    private Text minFloorText;

    @FXML
    void initialize() {
        elevatorCountText.setText(Long.toString(Math.round(elevatorCountSlider.getValue())));
        elevatorCountSlider.valueProperty().addListener(this::elevatorSliderListener);
        maxFloorText.setText(Long.toString(Math.round(maxFloorSlider.getValue())));
        maxFloorSlider.valueProperty().addListener(this::maxFloorSliderListener);
        minFloorText.setText(Long.toString(Math.round(minFloorSlider.getValue())));
        minFloorSlider.valueProperty().addListener(this::minFloorSliderListener);
    }

    public void initialize(int elevatorCount, int maxFloor, int minFloor) {
        elevatorCountSlider.setValue(elevatorCount);
        maxFloorSlider.setValue(maxFloor);
        minFloorSlider.setValue(minFloor);
    }

    private void elevatorSliderListener(Observable observable, Number oldValue, Number newValue) {
        int roundValue = roundSliderValue(elevatorCountSlider, newValue);
        elevatorCountText.setText(Integer.toString(roundValue));
    }

    private void maxFloorSliderListener(Observable observable, Number oldValue, Number newValue) {
        int roundValue = roundSliderValue(maxFloorSlider, newValue);
        maxFloorText.setText(Integer.toString(roundValue));
    }

    private void minFloorSliderListener(Observable observable, Number oldValue, Number newValue) {
        int roundValue = roundSliderValue(minFloorSlider, newValue);
        minFloorText.setText(Integer.toString(roundValue));
    }


    private int roundSliderValue(Slider slider, Number newValue) {
        long roundValue = Math.round(newValue.doubleValue());
        slider.setValue(roundValue);
        return (int) roundValue;
    }

    @FXML
    void pressedSimulationButton(ActionEvent event) {
        int elevatorsCount = (int) Math.round(elevatorCountSlider.getValue());
        int maxFloor = (int) Math.round(maxFloorSlider.getValue());
        int minFloor = (int) Math.round(minFloorSlider.getValue());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/fxml/Simulation.fxml"));
            Parent root = loader.load();
            SimulationView simulationView = loader.getController();
            simulationView.initialize(elevatorsCount, maxFloor, minFloor);

            mainPane.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
