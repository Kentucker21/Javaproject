package customerHelper;

import javax.swing.*;

import groupPackage.CustomerWindow;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;

public class ShipmentForm extends JFrame {

    private JTextField senderNameField, senderPhoneField, recipientNameField,
            recipientPhoneField, weightField, dimensionField, addressField;
    private JComboBox<String> typeCombo, zoneCombo;
    private JButton submitButton;
    private int customerId;
    private CustomerWindow parentWindow;
   

    public ShipmentForm(CustomerWindow parentWindow, int customerId) {
        super("Enter Shipment Details");
        this.customerId = customerId;
        this.parentWindow=parentWindow; //debug instance

        setLayout(new BorderLayout(10, 10));
        setSize(450, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // title
        JLabel title = new JLabel("Enter Shipment Details", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        //my main panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        senderNameField = new JTextField();
        senderPhoneField = new JTextField();
        recipientNameField = new JTextField();
        recipientPhoneField = new JTextField();
        weightField = new JTextField();
        dimensionField = new JTextField();
        typeCombo = new JComboBox<>(new String[]{"Standard", "Express", "Fragile"});
        addressField = new JTextField();
        zoneCombo = new JComboBox<>(new String[]{"Zone 1", "Zone 2", "Zone 3", "Zone 4"});

        formPanel.add(createLabeledField("Sender Name:", senderNameField));
        formPanel.add(createLabeledField("Sender Phone:", senderPhoneField));
        formPanel.add(createLabeledField("Recipient Name:", recipientNameField));
        formPanel.add(createLabeledField("Recipient Phone:", recipientPhoneField));
        formPanel.add(createLabeledField("Package Weight (kg):", weightField));
        formPanel.add(createLabeledField("Dimensions (cm):", dimensionField));
        formPanel.add(createLabeledField("Type:", typeCombo));
        formPanel.add(createLabeledField("Address:", addressField));
        formPanel.add(createLabeledField("Zone:", zoneCombo));

        // Send button
        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setPreferredSize(new Dimension(100, 40));
        submitButton.addActionListener(e -> submitShipment());

        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(submitButton);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);

        setVisible(true);
    }

    
    //helper label function
    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(label);
        panel.add(field);
        
        //adds padding
        panel.add(Box.createRigidArea(new Dimension(0, 8))); 
        return panel;
    }

    private void submitShipment() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String senderName = senderNameField.getText();
            String senderPhone = senderPhoneField.getText();
            String recipientName = recipientNameField.getText();
            String recipientPhone = recipientPhoneField.getText();
            double weight = Double.parseDouble(weightField.getText());
            String dimensions = dimensionField.getText();
            String type = (String) typeCombo.getSelectedItem();
            String destination = addressField.getText() + ", " + zoneCombo.getSelectedItem();
            String trackingNumber = "TRK" + new Random().nextInt(1000000);
            double cost = calculateCost(weight, zoneCombo.getSelectedIndex() + 1, type);

            String sql = "INSERT INTO shipments (customer_id, tracking_number, sender_name, sender_phone, recipient_name, recipient_phone, weight, dimensions, type, destination, cost, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Pending')";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, trackingNumber);
            ps.setString(3, senderName);
            ps.setString(4, senderPhone);
            ps.setString(5, recipientName);
            ps.setString(6, recipientPhone);
            ps.setDouble(7, weight);
            ps.setString(8, dimensions);
            ps.setString(9, type);
            ps.setString(10, destination);
            ps.setDouble(11, cost);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Shipment submitted! Tracking No: " + trackingNumber);
            
            //debug instance
            if (parentWindow != null) {
                parentWindow.refreshShipments();
            }

            
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private double calculateCost(double weight, int zone, String type) {
        double baseRate = 5.0;
        double zoneMultiplier = zone * 1.5;
        double typeMultiplier = switch (type) {
            case "Express" -> 2.0;
            case "Fragile" -> 1.5;
            default -> 1.0;
        };
        return weight * baseRate * zoneMultiplier * typeMultiplier;
    }
}
