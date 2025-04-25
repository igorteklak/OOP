import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

public class PredictorGUI extends JFrame {
    //Prediction section components
    JLabel titlePredictLabel = new JLabel("Predict Engine Status:");
    JLabel ignitionLabel = new JLabel("Ignition:");
    JLabel fuelLevelLabel = new JLabel("Fuel Level:");
    JLabel batteryChargedLabel = new JLabel("Battery Charged:");
    JLabel oilLevelLabel = new JLabel("Oil Level:");

    //Prediction dropdown boxes
    JComboBox<String> ignitionComboBox = new JComboBox<>(new String[]{"Yes", "No"});
    JComboBox<String> fuelLevelComboBox = new JComboBox<>(new String[]{"Yes", "No"});
    JComboBox<String> batteryChargedComboBox = new JComboBox<>(new String[]{"Yes", "No"});
    JComboBox<String> oilLevelComboBox = new JComboBox<>(new String[]{"Yes", "No"});

    //Add data section components (separate from prediction)
    JLabel titleAddDataLabel = new JLabel("Add New Data Point:");
    JLabel newIgnitionLabel = new JLabel("Ignition:");
    JLabel newFuelLevelLabel = new JLabel("Fuel Level:");
    JLabel newBatteryChargedLabel = new JLabel("Battery Charged:");
    JLabel newOilLevelLabel = new JLabel("Oil Level:");
    JLabel newEngineRunningLabel = new JLabel("Engine Running:");

    //Add data dropdown boxes
    JComboBox<String> newIgnitionComboBox = new JComboBox<>(new String[]{"Yes", "No"});
    JComboBox<String> newFuelLevelComboBox = new JComboBox<>(new String[]{"Yes", "No"});
    JComboBox<String> newBatteryChargedComboBox = new JComboBox<>(new String[]{"Yes", "No"});
    JComboBox<String> newOilLevelComboBox = new JComboBox<>(new String[]{"Yes", "No"});
    JComboBox<String> newEngineRunningComboBox = new JComboBox<>(new String[]{"Yes", "No"});

    //Buttons
    JButton predictButton = new JButton("Predict");
    JButton loadFileButton = new JButton("Load Data");
    JButton trainButton = new JButton("Train Model");
    JButton addDataButton = new JButton("Add Data");

    //Result and status labels
    JLabel resultLabel = new JLabel("Prediction: ");
    JLabel probabilityLabel = new JLabel("Probability: ");
    JLabel statusLabel = new JLabel("Status: Ready");
    JLabel filePathLabel = new JLabel("No file loaded");
    JLabel dataCountLabel = new JLabel("Data points: 0");

    public PredictorGUI() {
        //Set up the JFrame
        setTitle("Engine Predictor");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //Prediction
        JPanel predictionPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        predictionPanel.setBorder(BorderFactory.createTitledBorder("Prediction"));

        predictionPanel.add(ignitionLabel);
        predictionPanel.add(ignitionComboBox);

        predictionPanel.add(fuelLevelLabel);
        predictionPanel.add(fuelLevelComboBox);

        predictionPanel.add(batteryChargedLabel);
        predictionPanel.add(batteryChargedComboBox);

        predictionPanel.add(oilLevelLabel);
        predictionPanel.add(oilLevelComboBox);

        predictionPanel.add(predictButton);
        predictionPanel.add(new JLabel());

        predictionPanel.add(resultLabel);
        predictionPanel.add(probabilityLabel);

        //Data
        JPanel addDataPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        addDataPanel.setBorder(BorderFactory.createTitledBorder("Add New Data"));

        addDataPanel.add(newIgnitionLabel);
        addDataPanel.add(newIgnitionComboBox);

        addDataPanel.add(newFuelLevelLabel);
        addDataPanel.add(newFuelLevelComboBox);

        addDataPanel.add(newBatteryChargedLabel);
        addDataPanel.add(newBatteryChargedComboBox);

        addDataPanel.add(newOilLevelLabel);
        addDataPanel.add(newOilLevelComboBox);

        addDataPanel.add(newEngineRunningLabel);
        addDataPanel.add(newEngineRunningComboBox);

        addDataPanel.add(addDataButton);
        addDataPanel.add(dataCountLabel);

        //Training
        JPanel trainingPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        trainingPanel.setBorder(BorderFactory.createTitledBorder("Training"));

        trainingPanel.add(new JLabel("File:"));
        trainingPanel.add(filePathLabel);

        trainingPanel.add(loadFileButton);
        trainingPanel.add(trainButton);

        trainingPanel.add(new JLabel("Status:"));
        trainingPanel.add(statusLabel);

        //Add all panels to main panel
        mainPanel.add(predictionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        mainPanel.add(addDataPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        mainPanel.add(trainingPanel);

        //Add main panel to frame
        add(new JScrollPane(mainPanel));

        //Display the frame
        setVisible(true);
    }

    //Add action listeners
    public void addPredictListener(ActionListener listener) {
        predictButton.addActionListener(listener);
    }

    public void addTrainListener(ActionListener listener) {
        trainButton.addActionListener(listener);
    }

    public void addLoadFileListener(ActionListener listener) {
        loadFileButton.addActionListener(listener);
    }

    public void addAddDataListener(ActionListener listener) {
        addDataButton.addActionListener(listener);
    }

    //Get values from prediction dropdown boxes
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

    //Get values from add data dropdown boxes
    public String getNewIgnition() {
        return (String) newIgnitionComboBox.getSelectedItem();
    }

    public String getNewFuelLevel() {
        return (String) newFuelLevelComboBox.getSelectedItem();
    }

    public String getNewBatteryCharged() {
        return (String) newBatteryChargedComboBox.getSelectedItem();
    }

    public String getNewOilLevel() {
        return (String) newOilLevelComboBox.getSelectedItem();
    }

    public String getNewEngineRunning() {
        return (String) newEngineRunningComboBox.getSelectedItem();
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

    //Update status display
    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    //Update file path display
    public void updateFilePath(String path) {
        if (path.length() > 30) {
            // If path is too long, show only beginning and end
            path = path.substring(0, 15) + "..." +
                    path.substring(path.length() - 15);
        }
        filePathLabel.setText(path);
    }

    //Update data count
    public void updateDataCount(int count) {
        dataCountLabel.setText("Data points: " + count);
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

    //Show probability table in popup
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