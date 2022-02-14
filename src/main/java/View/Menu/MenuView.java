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
    void initialize() {
        elevatorCountText.setText(Long.toString(Math.round(elevatorCountSlider.getValue())));
        elevatorCountSlider.valueProperty().addListener(this::sliderListener);
    }

    public void initialize(int elevatorCount) {
        elevatorCountText.setText(Integer.toString(elevatorCount));
        elevatorCountSlider.setValue(elevatorCount);
    }

    private void sliderListener(Observable observable, Number oldValue, Number newValue) {
        elevatorCountSlider.setValue(Math.round(newValue.doubleValue()));
        long roundValue = Math.round(newValue.doubleValue());
        elevatorCountText.setText(Long.toString(roundValue));
    }

    @FXML
    void pressedSimulationButton(ActionEvent event) {
        int elevatorsCount = (int) Math.round(elevatorCountSlider.getValue());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/fxml/Simulation.fxml"));
            Parent root = loader.load();
            SimulationView simulationView = loader.getController();
            simulationView.initialize(elevatorsCount);

            mainPane.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
