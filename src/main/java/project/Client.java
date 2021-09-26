package project;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable{
    private final String serverHost;
    private final int serverPort;
    private int currentPlayer;
    private ObjectInputStream dIn;
    private ObjectOutputStream dOut;
    private ArrayList<String> gameOutputs;
    private Scanner scan;

    public Client(String host, int serverPort) {
        this.serverHost = host;
        this.serverPort = serverPort;
        gameOutputs = new ArrayList<>();
        scan = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Client client = new Client(Config.HOST, Config.SERVER_PORT_NUMBER);
        client.startClient();
    }

    /**
     * Starts the client, connects to server
     *
     */
    public void startClient() {
        try{
            System.out.println("Connecting to server...");

            Socket socket = new Socket(serverHost, serverPort);
            dOut = new ObjectOutputStream(socket.getOutputStream());
            dIn = new ObjectInputStream(socket.getInputStream());

            try{
                // Waiting for server response
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Connected to server");

            String startInfo = dIn.readUTF();
            System.out.println(startInfo);
            int playerID = dIn.readInt();

            String message = "";

            while (!message.equals("final_message")) {
                message = receiveUpdate();

                if (playerID == currentPlayer) {
                    System.out.println("Your action:");
                    // Reads player's action
                    StringBuilder action = new StringBuilder();

                    String input;

                    if (scan.hasNext()) {
                        input = scan.nextLine();

                        if (input.equals("draw")) {
                            action.append(input);
                        } else {
                            while (!input.equals("end")) {
                                action.append(input).append("\n");
                                input = scan.nextLine();
                            }
                        }

                        // Sends player inputs to the socket
                        sendAction(action.toString());
                    }
                }
            }

            scan.close();
            // Close socket
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sends action to server
     *
     */
    public void sendAction(String action) {
        try {
            dOut.writeUTF(action);
            dOut.flush();
        } catch (Exception e) {
            System.out.println("Could not send action");
            e.printStackTrace();
        }
    }

    /**
     * receive table and hand message
     *
     */
    public String receiveUpdate() {
        try {
            String msg = dIn.readUTF();
            currentPlayer = dIn.readInt();
            gameOutputs.add(msg);
            System.out.println(msg);
            return msg;
        } catch (Exception e) {
            System.out.println("Message not received");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * set user input for testing
     *
     */
    public void setInput(String input) {
        scan = new Scanner(input);
    }

    /**
     * get an output of server response
     *
     */
    public String getOutput(int i) {
        return gameOutputs.get(i);
    }

    /**
     * get size of outputs
     *
     */
    public int outputSize() {
        return gameOutputs.size();
    }

    /**
     * reset outputs for new game
     *
     */
    public void resetOutput() {
        gameOutputs.clear();
    }

    /**
     * Override Thread method
     *
     */
    @Override
    public void run() {
        startClient();
    }
}
