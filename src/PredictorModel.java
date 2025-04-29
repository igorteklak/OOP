import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictorModel {
    //Hardcoded probability table based on dataset
    private Map<String, Double> combinationProbabilities;

    //Lists to store training and testing data
    private List<String[]> trainingData;
    private List<String[]> testingData;

    // Column indices
    private final int IGNITION_INDEX = 0;
    private final int FUEL_LEVEL_INDEX = 1;
    private final int BATTERY_CHARGED_INDEX = 2;
    private final int OIL_LEVEL_INDEX = 3;
    private final int ENGINE_RUNNING_INDEX = 4;

    public PredictorModel() {
        //Initialize the probability table
        combinationProbabilities = new HashMap<>();

        //Initialize data lists
        trainingData = new ArrayList<>();
        testingData = new ArrayList<>();

        //Populate the map with initial probabilities
        initHardcodedProbabilities();

        //Try to load data files automatically
        loadTrainingDataFromFile();
        loadTestingDataFromFile();
    }

    //File names
    private final String TRAINING_FILE = "training_data.csv";
    private final String TESTING_FILE = "testing_data.csv";

    //Automatically load training data from file in current directory
    private boolean loadTrainingDataFromFile() {
        return loadDataFromFile(TRAINING_FILE, trainingData);
    }

    //Automatically load testing data from file in current directory
    private boolean loadTestingDataFromFile() {
        return loadDataFromFile(TESTING_FILE, testingData);
    }

    //Load data from a file into a list
    private boolean loadDataFromFile(String fileName, List<String[]> dataList) {
        dataList.clear();

        try {
            java.io.File file = new java.io.File(fileName);
            if (!file.exists()) {
                System.out.println("File not found: " + fileName);
                return false;
            }

            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                boolean isFirstLine = true;

                while ((line = br.readLine()) != null) {
                    //Skip header row
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    //Split by tab or comma (handles both formats)
                    String[] values;
                    if (line.contains("\t")) {
                        values = line.split("\t");
                    } else {
                        values = line.split(",");
                    }

                    //Add to data list if valid
                    if (values.length >= 5) {
                        dataList.add(values);
                    }
                }

                System.out.println("Loaded " + dataList.size() + " rows from " + fileName);
                return true;
            }
        } catch (java.io.IOException e) {
            System.out.println("Error loading data from " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    //Initialize with hardcoded probabilities, this was made using AI on my dataset to make things easier
    //The last digit represents the probability, 1.0 being 100%, 0.8 being 80% etc.
    private void initHardcodedProbabilities() {
        combinationProbabilities.put("Yes_Yes_Yes_Yes", 1.0);      // 100.00%
        combinationProbabilities.put("Yes_Yes_Yes_No", 0.8);       // 80.00%
        combinationProbabilities.put("Yes_Yes_No_Yes", 0.8824);    // 88.24%
        combinationProbabilities.put("Yes_Yes_No_No", 0.6667);     // 66.67%
        combinationProbabilities.put("Yes_No_Yes_Yes", 0.8667);    // 86.67%
        combinationProbabilities.put("Yes_No_Yes_No", 0.4545);     // 45.45%
        combinationProbabilities.put("Yes_No_No_Yes", 0.1875);     // 18.75%
        combinationProbabilities.put("Yes_No_No_No", 0.1667);      // 16.67%
        combinationProbabilities.put("No_Yes_Yes_Yes", 0.8462);    // 84.62%
        combinationProbabilities.put("No_Yes_Yes_No", 0.2308);     // 23.08%
        combinationProbabilities.put("No_Yes_No_Yes", 0.1429);     // 14.29%
        combinationProbabilities.put("No_Yes_No_No", 0.1429);      // 14.29%
        combinationProbabilities.put("No_No_Yes_Yes", 0.25);       // 25.00%
        combinationProbabilities.put("No_No_Yes_No", 0.1538);      // 15.38%
        combinationProbabilities.put("No_No_No_Yes", 0.2857);      // 28.57%
        combinationProbabilities.put("No_No_No_No", 0.2);          // 20.00%
    }

    //Add a new data point to the training dataset
    public void addDataPoint(String ignition, String fuelLevel, String batteryCharged,
                             String oilLevel, String engineRunning, boolean addToTraining) {
        String[] newDataPoint = new String[5];
        newDataPoint[IGNITION_INDEX] = ignition;
        newDataPoint[FUEL_LEVEL_INDEX] = fuelLevel;
        newDataPoint[BATTERY_CHARGED_INDEX] = batteryCharged;
        newDataPoint[OIL_LEVEL_INDEX] = oilLevel;
        newDataPoint[ENGINE_RUNNING_INDEX] = engineRunning;

        if (addToTraining) {
            trainingData.add(newDataPoint);
        } else {
            testingData.add(newDataPoint);
        }

        //Automatically recalculate probabilities if this is a training data point
        if (addToTraining) {
            calculateProbabilities();
        }
    }

    //Calculate probabilities from training data
    public boolean calculateProbabilities() {
        if (trainingData.isEmpty()) {
            return false;
        }

        //Clear existing probabilities
        combinationProbabilities.clear();

        //Count occurrences of each feature combination
        Map<String, int[]> combinationCounts = new HashMap<>();

        for (String[] row : trainingData) {
            String ignition = row[IGNITION_INDEX];
            String fuelLevel = row[FUEL_LEVEL_INDEX];
            String batteryCharged = row[BATTERY_CHARGED_INDEX];
            String oilLevel = row[OIL_LEVEL_INDEX];
            String engineRunning = row[ENGINE_RUNNING_INDEX];

            //Create the combination key
            String key = ignition + "_" + fuelLevel + "_" + batteryCharged + "_" + oilLevel;

            //Get or create the count array [yes_count, total_count]
            int[] counts = combinationCounts.getOrDefault(key, new int[]{0, 0});

            //Increment counts
            if (engineRunning.equals("Yes")) {
                counts[0]++;
            }
            counts[1]++;

            //Update the map
            combinationCounts.put(key, counts);
        }

        //Calculate probabilities
        for (Map.Entry<String, int[]> entry : combinationCounts.entrySet()) {
            String key = entry.getKey();
            int[] counts = entry.getValue();

            //Calculate probability (yes_count / total_count)
            double probability = counts[1] > 0 ? (double) counts[0] / counts[1] : 0;

            //Store in probabilities map
            combinationProbabilities.put(key, probability);
        }

        return true;
    }

    //Calculate accuracy on testing data
    public AccuracyResult calculateAccuracy() {
        if (testingData.isEmpty()) {
            return new AccuracyResult(0, 0, 0);
        }

        int totalTestCases = testingData.size();
        int correctPredictions = 0;

        for (String[] row : testingData) {
            String ignition = row[IGNITION_INDEX];
            String fuelLevel = row[FUEL_LEVEL_INDEX];
            String batteryCharged = row[BATTERY_CHARGED_INDEX];
            String oilLevel = row[OIL_LEVEL_INDEX];
            String actualEngineRunning = row[ENGINE_RUNNING_INDEX];

            //Make prediction
            PredictionResult prediction = predict(ignition, fuelLevel, batteryCharged, oilLevel);

            //Check if prediction matches actual value
            if (prediction.getPrediction().equals(actualEngineRunning)) {
                correctPredictions++;
            }
        }

        //Calculate accuracy percentage
        double accuracyPercentage = (double) correctPredictions / totalTestCases * 100;

        return new AccuracyResult(correctPredictions, totalTestCases, accuracyPercentage);
    }

    //Get training data size
    public int getTrainingDataSize() {
        return trainingData.size();
    }

    //Get testing data size
    public int getTestingDataSize() {
        return testingData.size();
    }

    //Get all calculated probabilities
    public Map<String, Double> getAllProbabilities() {
        return new HashMap<>(combinationProbabilities);
    }

    //Make prediction based on feature values
    public PredictionResult predict(String ignition, String fuelLevel, String batteryCharged, String oilLevel) {
        //Create the key to look up in our probability table
        String key = ignition + "_" + fuelLevel + "_" + batteryCharged + "_" + oilLevel;

        //Look up the probability of the engine running given these features
        Double probability = combinationProbabilities.get(key);

        if (probability == null) {
            //Error handling for missing probability, shouldn't be used as the dataset is full.
            return new PredictionResult("Unknown", 0.0);
        }

        //Prediction based on probability threshold
        String prediction = probability > 0.5 ? "Yes" : "No";

        return new PredictionResult(prediction, probability);
    }

    //Class to hold the prediction result
    public static class PredictionResult {
        private String prediction;
        private double probability;

        public PredictionResult(String prediction, double probability) {
            this.prediction = prediction;
            this.probability = probability;
        }

        public String getPrediction() {
            return prediction;
        }

        public double getProbability() {
            return probability;
        }
    }

    //Class to hold accuracy calculation results
    public static class AccuracyResult {
        private int correctPredictions;
        private int totalPredictions;
        private double accuracyPercentage;

        public AccuracyResult(int correctPredictions, int totalPredictions, double accuracyPercentage) {
            this.correctPredictions = correctPredictions;
            this.totalPredictions = totalPredictions;
            this.accuracyPercentage = accuracyPercentage;
        }

        public int getCorrectPredictions() {
            return correctPredictions;
        }

        public int getTotalPredictions() {
            return totalPredictions;
        }

        public double getAccuracyPercentage() {
            return accuracyPercentage;
        }
    }
}