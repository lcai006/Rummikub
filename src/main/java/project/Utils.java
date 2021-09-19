package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Handles networking message exchange
 *
 */
public class Utils {

    /**
     * Writes message to sockets DataOutputStream
     *
     */
    public static void writeMessage(PrintWriter pw, String message) throws IOException {
        pw.println(message);
        pw.flush();
        pw.close();
    }

    /**
     * Read message from sockets DataInputStream
     *
     */
    public static String readMessage(BufferedReader buf) throws IOException {
        StringBuilder msg = new StringBuilder();
        String line = buf.readLine();
        while(line != null) {
            msg.append(line).append("\n");
            line = buf.readLine();
        }

        return msg.toString();
    }

}
