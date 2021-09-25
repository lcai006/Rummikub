package project;

import model.Board;
import model.Hand;
import model.Meld;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class GameTest {
    static Server server;
    static ArrayList<Client> clients;

    @BeforeAll
    public static void setupGame() {
        server = new Server();
        clients = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Client c = new Client(Config.HOST, Config.SERVER_PORT_NUMBER);
            clients.add(c);
        }
        Thread serverThread = new Thread(server);
        serverThread.start();
        for (Client c: clients) {
            Thread thread = new Thread(c);
            thread.start();
        }
    }

    @DisplayName("test player sequence and UI updates")
    @Test
    public void testPlayerSequence() {
        String tiles1 = "R11 R12 R13";
        String tiles2 = "R13 B13 G13";
        String tiles3 = "G2 R2 O2";
        String tiles4 = "R12 B12 O12";

        server.reset(tiles4, tiles1, tiles2+" "+tiles3);
        String out = clients.get(0).getOutput();
        Board b = new Board(out);
        assertEquals("Player 1’s turn", b.turnInfo());

        // P1 draws
        clients.get(0).setInput("draw\n");
        out = clients.get(0).getOutput();
        b = new Board(out);
        assertEquals("Player 2’s turn", b.turnInfo());
        assertEquals(0, b.tableSize());
        assertEquals(15, b.handSize());

        // P2 plays {JH QH KH}
        out = clients.get(1).getOutput();
        b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play(tiles1);
        clients.get(1).setInput("new " + tiles1 + "\nend\n");
        out = clients.get(1).getOutput();
        b = new Board(out);
        assertEquals("Player 3’s turn", b.turnInfo());
        assertEquals(1, b.tableSize());
        assertEquals(11, b.handSize());
        assertEquals(tiles1, b.getMeld(0));
        assertEquals(oldHand.toString(), b.getHand());

        // P3 plays {KH KS KC} and {2C 2H 2D}
        out = clients.get(2).getOutput();
        b = new Board(out);
        oldHand = new Hand(b.getHand());
        oldHand.play(tiles2+" "+tiles3);
        clients.get(2).setInput("new " + tiles2 + "\n" + "new " + tiles3 + "\n" + "end\n");
        out = clients.get(2).getOutput();
        b = new Board(out);
        assertEquals("Player 1’s turn", b.turnInfo());
        assertEquals(3, b.tableSize());
        assertEquals(8, b.handSize());
        assertEquals(tiles2, b.getMeld(1));
        assertEquals(tiles3, b.getMeld(2));
        assertEquals(oldHand.toString(), b.getHand());

        // P3 plays {QH QS QD}
        out = clients.get(0).getOutput();
        b = new Board(out);
        oldHand = new Hand(b.getHand());
        oldHand.play(tiles4);
        clients.get(0).setInput("new " + tiles4 + "\nend\n");
        out = clients.get(0).getOutput();
        b = new Board(out);
        assertEquals("Player 2’s turn", b.turnInfo());
        assertEquals(0, b.tableSize());
        assertEquals(12, b.handSize());
        assertEquals(tiles1, b.getMeld(3));
        assertEquals(oldHand.toString(), b.getHand());
    }

}
