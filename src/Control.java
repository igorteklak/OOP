import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Control implements ActionListener {
    private final PredictorModel model;
    private final PredictorGUI gui;

    public Control() {
        //Initialize the model and GUI
        model = new PredictorModel();
        gui = new PredictorGUI();

        //Set up the action listeners
        gui.addPredictListener(this);
        gui.addTrainListener(this);
        gui.addAddDataListener(this);
        gui.addTestAccuracyListener(this);

        updateDataCounts();
    }

    //Update the GUI with current data counts
    private void updateDataCounts() {
        gui.updateDataCounts(model.getTrainingDataSize(), model.getTestingDataSize());

        if (model.getTrainingDataSize() > 0 || model.getTestingDataSize() > 0) {
            gui.updateStatus("Data loaded: " + model.getTrainingDataSize() +
                    " training, " + model.getTestingDataSize() + " testing");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gui.predictButton) {
            handlePredict();
        } else if (e.getSource() == gui.trainButton) {
            handleTrain();
        } else if (e.getSource() == gui.addDataButton) {
            handleAddData();
        } else if (e.getSource() == gui.testAccuracyButton) {
            handleTestAccuracy();
        }
    }

    // Handle predict button click
    private void handlePredict() {
        //Get the selected values from the GUI
        String ignition = gui.getIgnition();
        String fuelLevel = gui.getFuelLevel();
        String batteryCharged = gui.getBatteryCharged();
        String oilLevel = gui.getOilLevel();

        //Make the prediction using the model
        PredictorModel.PredictionResult result = model.predict(ignition, fuelLevel, batteryCharged, oilLevel);

        //Check if we have a valid prediction
        if (result.getPrediction().equals("Unknown")) {
            gui.updateStatus("Error: No data available for this combination");
            return;
        }

        //Update the GUI with the results
        gui.updateResults(result.getPrediction(), result.getProbability());

        //Show a popup with the result
        gui.showResultPopup(result.getPrediction(), result.getProbability());
    }

    //Handle train button click
    private void handleTrain() {
        if (model.getTrainingDataSize() == 0) {
            gui.updateStatus("Error: No training data loaded or added");
            return;
        }

        gui.updateStatus("Training model...");

        //Calculate probabilities from training data
        boolean success = model.calculateProbabilities();

        if (success) {
            int dataSize = model.getTrainingDataSize();
            gui.updateStatus("Model trained successfully with " + dataSize + " data points");
            updateDataCounts();

            //Show probability table
            gui.showProbabilityTable(model.getAllProbabilities());
        } else {
            gui.updateStatus("Error: Failed to train model");
        }
    }

    //Handle add data button click
    private void handleAddData() {
        //Get the values from the add data section
        String ignition = gui.getNewIgnition();
        String fuelLevel = gui.getNewFuelLevel();
        String batteryCharged = gui.getNewBatteryCharged();
        String oilLevel = gui.getNewOilLevel();
        String engineRunning = gui.getNewEngineRunning();

        //Add the new data point to the training data
        model.addDataPoint(ignition, fuelLevel, batteryCharged, oilLevel, engineRunning, true);

        //Update the data counts
        updateDataCounts();

        //Update status
        gui.updateStatus("Added new data point to training dataset: " +
                ignition + "_" + fuelLevel + "_" +
                batteryCharged + "_" + oilLevel + " = " + engineRunning);
    }

    //Handle test accuracy button click
    private void handleTestAccuracy() {
        if (model.getTestingDataSize() == 0) {
            gui.updateStatus("Error: No testing data available");
            return;
        }

        gui.updateStatus("Testing model accuracy...");

        //Calculate accuracy
        PredictorModel.AccuracyResult result = model.calculateAccuracy();

        //Update the GUI
        gui.updateAccuracy(result.getCorrectPredictions(), result.getTotalPredictions(),
                result.getAccuracyPercentage());

        //Show popup
        gui.showAccuracyPopup(result.getCorrectPredictions(), result.getTotalPredictions(),
                result.getAccuracyPercentage());

        //Update status
        gui.updateStatus(String.format("Accuracy: %.2f%% (%d/%d correct)",
                result.getAccuracyPercentage(),
                result.getCorrectPredictions(),
                result.getTotalPredictions()));
    }

    //Main method to start the application
    public static void main(String[] args) {
        new Control();
    }
}