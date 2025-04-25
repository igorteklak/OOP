import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Control implements ActionListener {
    private final PredictorModel model;
    private final PredictorGUI gui;
    private String trainingFilePath;

    public Control() {
        //Initialize the model and GUI
        model = new PredictorModel();
        gui = new PredictorGUI();

        //Set up the action listeners
        gui.addPredictListener(this);
        gui.addTrainListener(this);
        gui.addLoadFileListener(this);
        gui.addAddDataListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gui.predictButton) {
            handlePredict();
        } else if (e.getSource() == gui.trainButton) {
            handleTrain();
        } else if (e.getSource() == gui.loadFileButton) {
            handleLoadFile();
        } else if (e.getSource() == gui.addDataButton) {
            handleAddData();
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
        if (trainingFilePath == null && model.getTrainingDataSize() == 0) {
            gui.updateStatus("Error: No training data loaded or added");
            return;
        }

        gui.updateStatus("Training model...");

        //Calculate probabilities from training data
        boolean success = model.calculateProbabilities();

        if (success) {
            int dataSize = model.getTrainingDataSize();
            gui.updateStatus("Model trained successfully with " + dataSize + " data points");
            gui.updateDataCount(dataSize);

            //Show probability table
            gui.showProbabilityTable(model.getAllProbabilities());
        } else {
            gui.updateStatus("Error: Failed to train model");
        }
    }

    //Handle load file button click
    private void handleLoadFile() {
        String filePath = gui.showFileChooser();

        if (filePath != null) {
            trainingFilePath = filePath;
            gui.updateFilePath(filePath);
            gui.updateStatus("Loading training data...");

            //Load training data
            boolean success = model.loadTrainingData(filePath);

            if (success) {
                int dataSize = model.getTrainingDataSize();
                gui.updateStatus("Loaded " + dataSize + " training data points");
                gui.updateDataCount(dataSize);
            } else {
                gui.updateStatus("Error: Failed to load training data");
            }
        }
    }

    //Handle add data button click
    private void handleAddData() {
        //Get values from the add data section
        String ignition = gui.getNewIgnition();
        String fuelLevel = gui.getNewFuelLevel();
        String batteryCharged = gui.getNewBatteryCharged();
        String oilLevel = gui.getNewOilLevel();
        String engineRunning = gui.getNewEngineRunning();

        //Add the new data point to the model
        model.addDataPoint(ignition, fuelLevel, batteryCharged, oilLevel, engineRunning);

        //Update the data count
        int dataSize = model.getTrainingDataSize();
        gui.updateDataCount(dataSize);

        //Update status
        gui.updateStatus("Added new data point: " + ignition + "_" + fuelLevel + "_" +
                batteryCharged + "_" + oilLevel + " = " + engineRunning);

        //Optionally recalculate probabilities immediately
        model.calculateProbabilities();
    }

    //Main method to start the application
    public static void main(String[] args) {
        new Control();
    }
}