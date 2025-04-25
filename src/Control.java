import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Control implements ActionListener {
    private final PredictorModel model;
    private final PredictorGUI gui;

    public Control() {
        //Initialize the model and GUI
        model = new PredictorModel();
        gui = new PredictorGUI();

        //Set up the action listener for the predict button
        gui.addPredictListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Get the selected values from the GUI
        String ignition = gui.getIgnition();
        String fuelLevel = gui.getFuelLevel();
        String batteryCharged = gui.getBatteryCharged();
        String oilLevel = gui.getOilLevel();

        //Make the prediction using the model
        PredictorModel.PredictionResult result = model.predict(ignition, fuelLevel, batteryCharged, oilLevel);

        //Update the GUI with the results
        gui.updateResults(result.getPrediction(), result.getProbability());

        //Show a popup with the result
        gui.showResultPopup(result.getPrediction(), result.getProbability());
    }

    //Main method to start the application
    public static void main(String[] args) {
        new Control();
    }
}