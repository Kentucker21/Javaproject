package customerHelper;

import java.io.*;
import java.net.*;
import java.util.Collections;
import java.util.List;

public class ClientConnection {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1234;

    // GET Shipments
    public static List<Shipment> getShipments(int customerId) {
        try (
        	//opens new client socket
        	Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        	 
        	//opens output and input stream
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Send command and customerId
            out.writeObject("GET_SHIPMENTS");
            out.writeObject(customerId);
            out.flush();

            // Read server response
            Object response = in.readObject();
            
            //checks if response is of type list 
            if (response instanceof List<?>) {
                return (List<Shipment>) response;
            } else {
                System.err.println("Unexpected response from server: " + response);
            }

        } catch (EOFException e) { //catches end of file 
            System.err.println("Server closed connection unexpectedly.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList(); // just in case list is empty return something
    }

    // ADD Shipment
    public static String addShipment(Shipment shipment) {
        try (
        	//Socket to establish connection to server
        	Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
        		// input output stream 
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
           
        	//outputs command and shipment object to server
            out.writeObject("ADD_SHIPMENT");
            out.writeObject(shipment);
            out.flush();
            
            //catches server response
            Object response = in.readObject();
            if (response instanceof String) {
                return (String) response;
            }

        } catch (EOFException e) {
            System.err.println("Server closed connection unexpectedly.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ERROR: No response from server";
    }
}
