import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

public class PredictorGUI extends JFrame {
    //GUI components
    JLabel ignitionLabel = new JLabel("Ignition:");
    JLabel fuelLevelLabel = new JLabel("Fuel Level:");
    JLabel batteryChargedLabel = new JLabel("Battery Charged:");
    JLabel oilLevelLabel = new JLabel("Oil Level:");

    //Boxes for feature selection
    JComboBox<String> ignitionComboBox = new JComboBox<>(new String[]{"Yes", "No"});
    JComboBox<String> fuelLevelComboBox = new JComboBox<>(new String[]{"Yes", "No"});
    JComboBox<String> batteryChargedComboBox = new JComboBox<>(new String[]{"Yes", "No"});
    JComboBox<String> oilLevelComboBox = new JComboBox<>(new String[]{"Yes", "No"});

    //Button
    JButton predictButton = new JButton("Predict");
    JButton loadFileButton = new JButton("Load Data");
    JButton trainButton = new JButton("Train Model");

    //Result labels
    JLabel resultLabel = new JLabel("Prediction: ");
    JLabel probabilityLabel = new JLabel("Probability: ");
    JLabel statusLabel = new JLabel("Status: Ready");
    JLabel filePathLabel = new JLabel("No file loaded");

    public PredictorGUI() {
        // Set up the JFrame
        setTitle("Engine Predictor");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2));

        //Add components to panel
        panel.add(ignitionLabel);
        panel.add(ignitionComboBox);

        panel.add(fuelLevelLabel);
        panel.add(fuelLevelComboBox);

        panel.add(batteryChargedLabel);
        panel.add(batteryChargedComboBox);

        panel.add(oilLevelLabel);
        panel.add(oilLevelComboBox);

        panel.add(predictButton);
        panel.add(new JLabel());

        panel.add(resultLabel);
        panel.add(new JLabel());

        panel.add(probabilityLabel);

        panel.add(new JLabel("Training Data:"));
        panel.add(filePathLabel);

        panel.add(loadFileButton);
        panel.add(trainButton);

        panel.add(statusLabel);
        panel.add(new JLabel());

        //Add panel to frame
        add(panel);

        //Display the frame
        setVisible(true);
    }

    // Add action listener to predict button
    public void addPredictListener(ActionListener listener) {
        predictButton.addActionListener(listener);
    }

    //Add action listener to train and load file buttons
    public void addTrainListener(ActionListener listener) {
        trainButton.addActionListener(listener);
    }

    public void addLoadFileListener(ActionListener listener) {
        loadFileButton.addActionListener(listener);
    }

    //Get values from boxes
    public String getIgnition() {
        return (String) ignitionComboBox.getSelectedItem();
    }

    public String getFuelLevel() {
        return (String) fuelLevelComboBox.getSelectedItem();
    }

    public String getBatteryCharged() {
        return (String) batteryChargedComboBox.getSelectedItem();
    }

    public String getOilLevel() {
        return (String) oilLevelComboBox.getSelectedItem();
    }

    //Update result display
    public void updateResults(String prediction, double probability) {
        resultLabel.setText("Prediction: " + prediction);
        probabilityLabel.setText("Probability: " + Math.round(probability * 100) + "%");
    }

    //Show popup with result
    public void showResultPopup(String prediction, double probability) {
        JOptionPane.showMessageDialog(
                this,
                "Engine Running: " + prediction + "\n" +
                        "Probability: " + Math.round(probability * 100) + "%",
                "Result",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void updateStatus(String status) {
        statusLabel.setText("Status: " + status);
    }

    //Update file path display
    public void updateFilePath(String path) {
        filePathLabel.setText(path);
    }

    //Show file chooser dialog
    public String showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }

        return null;
    }

    // Show probability table in popup - simplified version
    public void showProbabilityTable(Map<String, Double> probabilities) {
        StringBuilder tableContent = new StringBuilder();
        tableContent.append("Probability Table:\n\n");

        for (Map.Entry<String, Double> entry : probabilities.entrySet()) {
            String key = entry.getKey();
            double prob = entry.getValue() * 100;
            tableContent.append(key + " = " + Math.round(prob) + "%\n");
        }

        JTextArea textArea = new JTextArea(20, 40);
        textArea.setText(tableContent.toString());
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Probabilities",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

