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

        //Set up the action listener for the predict button
        gui.addPredictListener(this);
        gui.addTrainListener(this);
        gui.addLoadFileListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gui.predictButton) {
            handlePredict();
        } else if (e.getSource() == gui.trainButton) {
            handleTrain();
        } else if (e.getSource() == gui.loadFileButton) {
            handleLoadFile();
        }
    }

    //Handle predict button click
    private void handlePredict() {
        //Get the selected values from the GUI
        String ignition = gui.getIgnition();
        String fuelLevel = gui.getFuelLevel();
        String batteryCharged = gui.getBatteryCharged();
        String oilLevel = gui.getOilLevel();

        //Make the prediction using the model
        PredictorModel.PredictionResult result = model.predict(ignition, fuelLevel, batteryCharged, oilLevel);

        //Check if prediction is valid
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
        if (trainingFilePath == null || trainingFilePath.isEmpty()) {
            gui.updateStatus("Error: No training file loaded");
            return;
        }

        gui.updateStatus("Training model...");

        //Calculate probabilities from training data
        boolean success = model.calculateProbabilities();

        if (success) {
            int dataSize = model.getTrainingDataSize();
            gui.updateStatus("Model trained successfully with " + dataSize + " data points");

            //Show probability table
            gui.showProbabilityTable(model.getAllProbabilities());
        } else {
            gui.updateStatus("Error: Failed to train model");
        }
    }

    // New for Level 2: Handle load file button click
    private void handleLoadFile() {
        String filePath = gui.showFileChooser();

        if (filePath != null) {
            trainingFilePath = filePath;
            gui.updateFilePath(filePath);
            gui.updateStatus("Loading training data...");

            // Load training data
            boolean success = model.loadTrainingData(filePath);

            if (success) {
                int dataSize = model.getTrainingDataSize();
                gui.updateStatus("Loaded " + dataSize + " training data points");
            } else {
                gui.updateStatus("Error: Failed to load training data");
            }
        }
    }

    //Main method to start the application
    public static void main(String[] args) {
        new Control();
    }
}