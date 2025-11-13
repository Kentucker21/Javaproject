package server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

import customerHelper.DatabaseConnection;
import customerHelper.Shipment;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            // Create OutputStream first, then InputStream
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            String command = (String) in.readObject();

            switch (command) {
                case "GET_SHIPMENTS" -> handleGetShipments(in, out);
                case "ADD_SHIPMENT" -> handleAddShipment(in, out);
                default -> {
                    out.writeObject("Unknown command");
                    out.flush();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close(); // ensure socket is closed after handling
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleGetShipments(ObjectInputStream in, ObjectOutputStream out) throws Exception {
        int customerId = (Integer) in.readObject();

        try (Connection con = DatabaseConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT tracking_number, type, destination, cost, status FROM shipments WHERE customer_id=?"
            );
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            List<Shipment> shipments = new ArrayList<>();
            while (rs.next()) {
                Shipment s = new Shipment();
                s.setTrackingNumber(rs.getString("tracking_number"));
                s.setType(rs.getString("type"));
                s.setDestination(rs.getString("destination"));
                s.setCost(rs.getDouble("cost"));
                s.setStatus(rs.getString("status"));
                shipments.add(s);
            }

            out.writeObject(shipments); // send list back to client
            out.flush();                // important!
        }
    }

    private void handleAddShipment(ObjectInputStream in, ObjectOutputStream out) throws Exception {
        Shipment shipment = (Shipment) in.readObject();

        try (Connection con = DatabaseConnection.getConnection()) {
            String sql = """
                INSERT INTO shipments (customer_id, tracking_number, sender_name, sender_phone,
                recipient_name, recipient_phone, weight, dimensions, type, destination, cost, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Pending')
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, shipment.getCustomerId());
            ps.setString(2, shipment.getTrackingNumber());
            ps.setString(3, shipment.getSenderName());
            ps.setString(4, shipment.getSenderPhone());
            ps.setString(5, shipment.getRecipientName());
            ps.setString(6, shipment.getRecipientPhone());
            ps.setDouble(7, shipment.getWeight());
            ps.setString(8, shipment.getDimensions());
            ps.setString(9, shipment.getType());
            ps.setString(10, shipment.getDestination());
            ps.setDouble(11, shipment.getCost());

            ps.executeUpdate();

            out.writeObject("Shipment added successfully!");
            out.flush(); // make sure client receives message
        }
    }
}
