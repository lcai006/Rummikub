package project;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private final String serverHost;
    private final int serverPort;
    private int currentPlayer;
    private ObjectInputStream dIn;
    private ObjectOutputStream dOut;

    private Client(String host, int serverPort) {
        this.serverHost = host;
        this.serverPort = serverPort;
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

            String message = dIn.readUTF();
            currentPlayer = dIn.readInt();
            while (!message.equals("final_message")) {
                message = receiveUpdate();

                if (playerID == currentPlayer) {
                    // Reads player's action
                    Scanner scan = new Scanner(System.in);
                    StringBuilder action = new StringBuilder();
                    String input = scan.nextLine();
                    if (input.equals("draw")) {
                        action.append(input);
                    } else {
                        while (!input.equals("end")) {
                            action.append(input);
                            input = scan.nextLine();
                        }
                    }
                    scan.close();

                    // Sends player inputs to the socket
                    sendAction(action.toString());
                }
            }

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
            System.out.println(msg);
            return msg;
        } catch (Exception e) {
            System.out.println("Message not received");
            e.printStackTrace();
        }
        return "";
    }

}
