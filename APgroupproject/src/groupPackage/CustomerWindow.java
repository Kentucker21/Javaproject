package groupPackage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import customerHelper.DatabaseConnection;
import customerHelper.ShipmentForm;

import java.awt.*;
import java.sql.*;

public class CustomerWindow extends JFrame {
    private User currentUser;
    private JButton makeShipmentButton;
    private JTable currentTable, completedTable;

    public CustomerWindow(User user) {
        super("Customer Dashboard");
        this.currentUser = user;

        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + " (ID: " + user.getId() + ")", SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        makeShipmentButton = new JButton("Make Shipment Request");
        
        //debug instance
        makeShipmentButton.addActionListener(e -> new ShipmentForm(this,user.getId()));
        
        centerPanel.add(makeShipmentButton, BorderLayout.NORTH);

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));

        // Current shipments
        JLabel currentLabel = new JLabel("Current Shipments", SwingConstants.LEFT);
        currentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tablesPanel.add(currentLabel);
        currentTable = new JTable();
        tablesPanel.add(new JScrollPane(currentTable));

        // Completed shipments
        JLabel completedLabel = new JLabel("Completed Shipments", SwingConstants.LEFT);
        completedLabel.setFont(new Font("Arial", Font.BOLD, 14));
        tablesPanel.add(completedLabel);
        completedTable = new JTable();
        tablesPanel.add(new JScrollPane(completedTable));

        centerPanel.add(tablesPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        refreshShipments();
        setVisible(true);
    }

    public void refreshShipments() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT tracking_number, type, destination, cost, status FROM shipments WHERE customer_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();

            
            	//Jtable outline
            DefaultTableModel currentModel = new DefaultTableModel(new String[]{"Tracking No", "Type", "Destination", "Cost", "Status"}, 0);
            DefaultTableModel completedModel = new DefaultTableModel(new String[]{"Tracking No", "Type", "Destination", "Cost", "Status"}, 0);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("tracking_number"),
                    rs.getString("type"),
                    rs.getString("destination"),
                    rs.getDouble("cost"),
                    rs.getString("status")
                };
                if (rs.getString("status").equalsIgnoreCase("Delivered"))
                    completedModel.addRow(row);
                else
                    currentModel.addRow(row);
            }

            currentTable.setModel(currentModel);
            completedTable.setModel(completedModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
}
