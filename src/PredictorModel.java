import java.util.HashMap;
import java.util.Map;

public class PredictorModel {
    //Hardcoded probability table based on my dataset
    private Map<String, Double> combinationProbabilities;

    public PredictorModel() {
        //Initialize the probability table
        combinationProbabilities = new HashMap<>();

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