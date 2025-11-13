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
        super("Enter Shipment Details"); //form instructions title
        this.customerId = customerId;
        this.parentWindow=parentWindow; //debug instance
        
        //shipment main layout is borderLayout
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
        //inner panel for the fields uses boxlayout
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
        
        //uses setAlignmentx to center along the x axis
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setPreferredSize(new Dimension(100, 40));
        submitButton.addActionListener(e -> submitShipment());

        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(submitButton);

        //adds the inner form layout to the center
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
        
        panel.add(field);
        panel.add(label);
        //adds padding
        panel.add(Box.createRigidArea(new Dimension(0, 8))); 
        return panel;
    }
    
    
    //sends to the server

    private void submitShipment() {
        try {
            Shipment shipment = new Shipment();
            shipment.setCustomerId(customerId);
            shipment.setSenderName(senderNameField.getText());
            shipment.setSenderPhone(senderPhoneField.getText());
            shipment.setRecipientName(recipientNameField.getText());
            shipment.setRecipientPhone(recipientPhoneField.getText());
            shipment.setWeight(Double.parseDouble(weightField.getText()));
            shipment.setDimensions(dimensionField.getText());
            shipment.setType((String) typeCombo.getSelectedItem());
            shipment.setDestination(addressField.getText() + ", " + zoneCombo.getSelectedItem());
            shipment.setTrackingNumber("TRK" + new Random().nextInt(1000000));
            shipment.setCost(calculateCost(shipment.getWeight(), zoneCombo.getSelectedIndex() + 1, shipment.getType()));

            String response = ClientConnection.addShipment(shipment);
            JOptionPane.showMessageDialog(this, response);

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
