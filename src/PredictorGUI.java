import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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

    //Result labels
    JLabel resultLabel = new JLabel("Prediction: ");
    JLabel probabilityLabel = new JLabel("Probability: ");

    public PredictorGUI() {
        // Set up the JFrame
        setTitle("Engine Predictor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

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

        //Add panel to frame
        add(panel);

        //Display the frame
        setVisible(true);
    }

    // Add action listener to predict button
    public void addPredictListener(ActionListener listener) {
        predictButton.addActionListener(listener);
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
}