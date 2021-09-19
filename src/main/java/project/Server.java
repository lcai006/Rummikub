package project;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private int serverPort;
    private ArrayList<Socket> clients;
    private ServerSocket serverSocket;

    public Server() {
        this.serverPort = Config.SERVER_PORT_NUMBER;
    }

    public static void main(String[] args) {
        Server server = new Server();
        System.out.println("Starting game server...");
        server.startServer();
    }

    /**
     * Starting the server and listen to the server port
     *
     */
    private void startServer(){
        System.out.println("Waiting for players...");
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }

        acceptClients();
        sendStartInfo();

        sendFinalMessage();
    }

    /**
     * Accepting client connections, the number of clients is limited
     *
     */
    private void acceptClients() {
        while (clients.size() < Config.NUM_OF_CLIENTS) {
            try{
                // Accept client, add to the list
                Socket socket = serverSocket.accept();
                clients.add(socket);
            } catch (IOException e) {
                System.out.println("Accept failed on: " + serverPort);
            }
        }
        System.out.println("Ready to start");
    }

    /**
     * Send string message to clients to end the program
     *
     */
    public void sendString(String str) {
        // Send messages to clients
        for (Socket client : clients) {
            if (!client.isClosed()) {
                try {
                    // sends output to the socket
                    Utils.writeMessage(new PrintWriter(client.getOutputStream(), true), str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Send information to clients to start the game
     *
     */
    public void sendStartInfo() {
        System.out.println("Sending start info to players");

        // Send messages to clients
        for(int i = 0; i < clients.size(); i++) {
            // Creating start message
            String startMessage = """
                    Three players are ready
                    Starting the game...
                    """;

            startMessage += "You are Player" + (i+1) + "\n";

            if(!clients.get(i).isClosed()) {
                try{
                    // sends output to the socket
                    Utils.writeMessage(new PrintWriter(clients.get(i).getOutputStream(), true), startMessage);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Send final message to clients to end the program
     *
     */
    public void sendFinalMessage() {
        System.out.println("Sending final message to players");

        // Creating final message
        String finalMessage = "final_message\n";

        // Send messages to clients
        sendString(finalMessage);
    }



}
