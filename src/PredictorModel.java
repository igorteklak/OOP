import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictorModel {
    //Hardcoded probability table based on my dataset
    private Map<String, Double> combinationProbabilities;

    // List to store training data
    private List<String[]> trainingData;

    // Column indices
    private final int IGNITION_INDEX = 0;
    private final int FUEL_LEVEL_INDEX = 1;
    private final int BATTERY_CHARGED_INDEX = 2;
    private final int OIL_LEVEL_INDEX = 3;
    private final int ENGINE_RUNNING_INDEX = 4;

    public PredictorModel() {
        //Initialize the probability table
        combinationProbabilities = new HashMap<>();
        // Initialize training data list
        trainingData = new ArrayList<>();

        //Populate the map with all combinations and probabilities, this was made using AI on my dataset to make things easier
        //The last digit represents the probability, 1.0 being 100%, 0.8 being 80% etc.
        combinationProbabilities.put("Yes_Yes_Yes_Yes", 1.0);
        combinationProbabilities.put("Yes_Yes_Yes_No", 0.8);
        combinationProbabilities.put("Yes_Yes_No_Yes", 0.8824);
        combinationProbabilities.put("Yes_Yes_No_No", 0.6667);
        combinationProbabilities.put("Yes_No_Yes_Yes", 0.8667);
        combinationProbabilities.put("Yes_No_Yes_No", 0.4545);
        combinationProbabilities.put("Yes_No_No_Yes", 0.1875);
        combinationProbabilities.put("Yes_No_No_No", 0.1667);
        combinationProbabilities.put("No_Yes_Yes_Yes", 0.8462);
        combinationProbabilities.put("No_Yes_Yes_No", 0.2308);
        combinationProbabilities.put("No_Yes_No_Yes", 0.1429);
        combinationProbabilities.put("No_Yes_No_No", 0.1429);
        combinationProbabilities.put("No_No_Yes_Yes", 0.25);
        combinationProbabilities.put("No_No_Yes_No", 0.1538);
        combinationProbabilities.put("No_No_No_Yes", 0.2857);
        combinationProbabilities.put("No_No_No_No", 0.2);
    }

    // Load training data from CSV file
    public boolean loadTrainingData(String filePath) {
        trainingData.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                //Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                //Split by tab or comma
                String[] values;
                if (line.contains("\t")) {
                    values = line.split("\t");
                } else {
                    values = line.split(",");
                }

                //Add to training data if valid
                if (values.length >= 5) {
                    trainingData.add(values);
                }
            }

            return true;
            //Catch case for errors
        } catch (IOException e) {
            System.out.println("Error loading training data: " + e.getMessage());
            return false;
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

            //Create combination key
            String key = ignition + "_" + fuelLevel + "_" + batteryCharged + "_" + oilLevel;

            //Get or create count array [yes_count, total_count]
            int[] counts = combinationCounts.getOrDefault(key, new int[]{0, 0});

            //Increment counts
            if (engineRunning.equals("Yes")) {
                counts[0]++;
            }
            counts[1]++;

            //Update map
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

    //Get training data size
    public int getTrainingDataSize() {
        return trainingData.size();
    }

    //Get all calculated probabilities
    public Map<String, Double> getAllProbabilities() {
        return new HashMap<>(combinationProbabilities);
    }

    //Make prediction based on feature values
    public PredictionResult predict(String ignition, String fuelLevel, String batteryCharged, String oilLevel) {
        // Create the key to look up in our probability table
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
}