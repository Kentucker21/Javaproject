package server;

import java.io.*;
import java.net.*;
import java.sql.*;
import customerHelper.DatabaseConnection;

public class Server {

    private ServerSocket serverSocket;

    public Server(int port) {
        try {
        	
        	//creates server socket and takes in port
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
   
            
            //used to continuously listen for client requests
            while (true) {
            	//connects client socket
                Socket clientSocket = serverSocket.accept();
                System.out.println(" Client connected: " + clientSocket.getInetAddress());
                
                //multithreading initialization
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(1234); // Server starts here
    }
}
