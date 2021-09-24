package project;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private final int serverPort;
    private ArrayList<Socket> clients;
    private ServerSocket serverSocket;
    private ArrayList<ObjectInputStream> dIn;
    private ArrayList<ObjectOutputStream> dOut;

    public Server() {
        this.serverPort = Config.SERVER_PORT_NUMBER;
        dIn = new ArrayList<>();
        dOut = new ArrayList<>();
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
    public void startServer(){
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

        // Game logic
        Game game = new Game();

        sendFinalMessage();
        while (true) {
        }
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
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                dOut.add(out);
                dIn.add(in);
            } catch (IOException e) {
                System.out.println("Accept failed on: " + serverPort);
            }
        }
        System.out.println("Ready to start");
    }

    /**
     * Update table and hand for players
     *
     */
    public void update(Game game) {
        String msg = "";

        // Game Board Message

        // Send messages to clients
        for (ObjectOutputStream out : dOut) {
            try {
                out.writeUTF(msg);
                out.writeInt(game.getCurrentPlayer());
                out.flush();
            } catch (Exception e) {
                System.out.println("Could not update information");
                e.printStackTrace();
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
        for(int i = 0; i < dOut.size(); i++) {
            // Creating start message
            String startMessage = """
                    Three players are ready
                    Starting the game...
                    """;

            startMessage += "You are Player" + (i+1) + "\n";

            try {
                dOut.get(i).writeUTF(startMessage);
                dOut.get(i).writeInt(i);
                dOut.get(i).flush();
            } catch (Exception e) {
                e.printStackTrace();
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
        String finalMessage = "final_message";

        // Send messages to clients
        for (ObjectOutputStream out : dOut) {
            try {
                out.writeUTF(finalMessage);
                out.writeInt(-1);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reset game
     *
     */
    public void reset() {

    }

}
