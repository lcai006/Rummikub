package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private String serverHost;
    private int serverPort;

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

            try{
                // Waiting for server response
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Connected to server");

            // Read start info message
            BufferedReader buf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String startInfo = Utils.readMessage(buf);
            System.out.println(startInfo);

            String message = Utils.readMessage(buf);
            while (!message.equals("final_message")) {
                message = Utils.readMessage(buf);

                // Reads player's action
                Scanner scan = new Scanner(System.in);
                String action = scan.nextLine();

                // Sends player inputs to the socket
                Utils.writeMessage(new PrintWriter(socket.getOutputStream(), true), action);
            }

            // Close buff reader and socket
            buf.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
