package project;

import model.*;
import org.junit.jupiter.api.*;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class GameTest {
    static Server server;
    static ArrayList<Client> clients;

    @BeforeAll
    public static void setupGame() throws InterruptedException {
        server = new Server(1);
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
            // make sure index matches player id
            Thread.sleep(10);
        }
        Thread.sleep(1000);
    }

    @DisplayName("test player sequence and UI updates")
    @Test
    public void testPlayerSequence() {
        String tiles1 = "R11 R12 R13";
        String tiles2 = "R13 B13 G13";
        String tiles3 = "G2 R2 O2";
        String tiles4 = "R12 B12 O12";

        clients.get(0).setInput("draw" + System.lineSeparator() + "new " + tiles4 + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("new " + tiles1 + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(2).setInput("new " + tiles2 + System.lineSeparator() + "new " + tiles3 + System.lineSeparator() + "end" + System.lineSeparator());
        server.reset(tiles4, tiles1, tiles2+" "+tiles3);

        // waiting for game
        await().until(() -> clients.get(2).outputSize() > 4);
        String out = clients.get(0).getOutput(0);
        Board b = new Board(out);
        assertEquals("Player 1’s turn", b.turnInfo());

        // P1 draws
        out = clients.get(0).getOutput(1);
        b = new Board(out);
        assertEquals("Player 2’s turn", b.turnInfo());
        assertEquals(0, b.tableSize());
        assertEquals(15, b.handSize());

        // P2 plays {JH QH KH}
        out = clients.get(1).getOutput(1);
        b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play(tiles1);
        out = clients.get(1).getOutput(2);
        b = new Board(out);
        assertEquals("Player 3’s turn", b.turnInfo());
        assertEquals(11, b.handSize());
        assertEquals("{*R11 *R12 *R13}", b.getMeld(0));
        assertEquals(oldHand.toString(), b.getHand());

        // P3 plays {KH KS KC} and {2C 2H 2D}
        out = clients.get(2).getOutput(2);
        b = new Board(out);
        oldHand = new Hand(b.getHand());
        oldHand.play(tiles2+" "+tiles3);
        out = clients.get(2).getOutput(3);
        b = new Board(out);
        assertEquals("Player 1’s turn", b.turnInfo());
        assertEquals(8, b.handSize());
        assertEquals("{R11 R12 R13}", b.getMeld(0));
        assertEquals("{*R13 *G13 *B13}", b.getMeld(1));
        assertEquals("{*R2 *G2 *O2}", b.getMeld(2));
        assertEquals(oldHand.toString(), b.getHand());

        // P3 plays {QH QS QD}
        out = clients.get(0).getOutput(3);
        b = new Board(out);
        oldHand = new Hand(b.getHand());
        oldHand.play(tiles4);
        out = clients.get(0).getOutput(4);
        b = new Board(out);
        assertEquals("Player 2’s turn", b.turnInfo());
        assertEquals(12, b.handSize());
        assertEquals("{*R12 *B12 *O12}", b.getMeld(3));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Nested
    class TestInitialPlay{
        @Test
        public void testInitialPlay1() {
            for (Client c: clients) {
                c.resetOutput();
            }

            String tiles = "R11 R12 R13";
            clients.get(0).setInput("new " + tiles + System.lineSeparator() + "end" + System.lineSeparator());
            server.reset(tiles, "", "");

            // waiting for game
            await().until(() -> clients.get(0).outputSize() > 1);
            String out = clients.get(0).getOutput(0);
            Board b = new Board(out);
            Hand oldHand = new Hand(b.getHand());
            oldHand.play(tiles);

            // P1 plays {JH QH KH}
            out = clients.get(0).getOutput(1);
            b = new Board(out);
            assertEquals(11, b.handSize());
            assertEquals("{*R11 *R12 *R13}", b.getMeld(0));
            assertEquals(oldHand.toString(), b.getHand());
        }

        @Test
        public void testInitialPlay2() {
            for (Client c: clients) {
                c.resetOutput();
            }

            String tiles = "R12 G12 B12";
            clients.get(0).setInput("new " + tiles + System.lineSeparator() + "end" + System.lineSeparator());
            server.reset(tiles, "", "");

            // waiting for game
            await().until(() -> clients.get(0).outputSize() > 1);
            String out = clients.get(0).getOutput(0);
            Board b = new Board(out);
            Hand oldHand = new Hand(b.getHand());
            oldHand.play(tiles);

            // P1 plays {QH QC QS}
            out = clients.get(0).getOutput(1);
            b = new Board(out);
            assertEquals(11, b.handSize());
            assertEquals("{*R12 *G12 *B13}", b.getMeld(0));
            assertEquals(oldHand.toString(), b.getHand());
        }

        @Test
        public void testInitialPlay3() {
            for (Client c: clients) {
                c.resetOutput();
            }

            String tiles = "R9 R10 R11 R12 R13";
            clients.get(0).setInput("new " + tiles + System.lineSeparator() + "end" + System.lineSeparator());
            server.reset(tiles, "", "");

            // waiting for game
            await().until(() -> clients.get(0).outputSize() > 1);
            String out = clients.get(0).getOutput(0);
            Board b = new Board(out);
            Hand oldHand = new Hand(b.getHand());
            oldHand.play(tiles);

            // P1 plays {9H 10H JH QH KH}
            out = clients.get(0).getOutput(1);
            b = new Board(out);
            assertEquals(9, b.handSize());
            assertEquals("{*R9 *R10 *R11 *R12 *R13}", b.getMeld(0));
            assertEquals(oldHand.toString(), b.getHand());
        }

        @Test
        public void testInitialPlay4() {
            for (Client c: clients) {
                c.resetOutput();
            }

            String tiles = "R13 G13 B13 O13";
            clients.get(0).setInput("new " + tiles + System.lineSeparator() + "end" + System.lineSeparator());
            server.reset(tiles, "", "");

            // waiting for game
            await().until(() -> clients.get(0).outputSize() > 1);
            String out = clients.get(0).getOutput(0);
            Board b = new Board(out);
            Hand oldHand = new Hand(b.getHand());
            oldHand.play(tiles);

            // P1 plays {KH KC KS KD}
            out = clients.get(0).getOutput(1);
            b = new Board(out);
            assertEquals(10, b.handSize());
            assertEquals("{*R13 *G13 *B13 *O13}", b.getMeld(0));
            assertEquals(oldHand.toString(), b.getHand());
        }
    }

}
