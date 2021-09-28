package project;

import model.*;
import org.junit.jupiter.api.*;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

public class GameTest {
    static Server server;
    static ArrayList<Client> clients;

    @BeforeEach
    public void setupGame() throws InterruptedException {
        server = new Server(1);
        clients = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Client c = new Client(Config.HOST, Config.SERVER_PORT_NUMBER);
            clients.add(c);
        }
        Thread serverThread = new Thread(server);
        serverThread.start();
        Thread.sleep(20);
        for (Client c : clients) {
            Thread thread = new Thread(c);
            thread.start();
            // make sure index matches player id
            Thread.sleep(10);
        }
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
        server.reset(tiles4, tiles1, tiles2 + " " + tiles3);

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
        oldHand.play(tiles2 + " " + tiles3);
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

    @Test
    public void testInitialPlay1() {
        String tiles = "R11 R12 R13";
        clients.get(0).setInput("new " + tiles + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
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
        String tiles = "R12 G12 B12";
        clients.get(0).setInput("new " + tiles + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
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
        assertEquals("{*R12 *G12 *B12}", b.getMeld(0));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Test
    public void testInitialPlay3() {
        String tiles = "R9 R10 R11 R12 R13";
        clients.get(0).setInput("new " + tiles + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
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
        String tiles = "R13 G13 B13 O13";
        clients.get(0).setInput("new " + tiles + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
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

    @Test
    public void testInitialPlay5() {
        String tiles1 = "R2 R3 R4";
        String tiles2 = "B7 B8 B9";
        clients.get(0).setInput("new " + tiles1 + System.lineSeparator() + "new " + tiles2 + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
        server.reset(tiles1 + " " + tiles2, "", "");

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 1);
        String out = clients.get(0).getOutput(0);
        Board b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play(tiles1 + " " + tiles2);

        // P1 plays {2H 3H 4H} {7S 8S 9S}
        out = clients.get(0).getOutput(1);
        b = new Board(out);
        assertEquals(8, b.handSize());
        assertEquals("{*R2 *R3 *R4}", b.getMeld(0));
        assertEquals("{*B7 *B8 *B9}", b.getMeld(1));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Test
    public void testInitialPlay6() {
        String tiles1 = "R2 B2 O2";
        String tiles2 = "G4 O4 B4 R4";
        String tiles3 = "O5 B5 R5";
        clients.get(0).setInput("new " + tiles1 + System.lineSeparator() + "new " + tiles2 + System.lineSeparator() + "new " + tiles3 + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
        server.reset(tiles1 + " " + tiles2 + " " + tiles3, "", "");

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 1);
        String out = clients.get(0).getOutput(0);
        Board b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play(tiles1 + " " + tiles2+ " " + tiles3);

        // P1 plays {2H 2S 2D} {4C 4D 4S 4H} {5D 5S 5H}
        out = clients.get(0).getOutput(1);
        b = new Board(out);
        assertEquals(4, b.handSize());
        assertEquals("{*R2 *B2 *O2}", b.getMeld(0));
        assertEquals("{*R4 *G4 *B4 *O4}", b.getMeld(1));
        assertEquals("{*R5 *B5 *O5}", b.getMeld(2));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Test
    public void testInitialPlay7() {
        String tiles1 = "R8 G8 O8";
        String tiles2 = "R2 R3 R4";
        clients.get(0).setInput("new " + tiles1 + System.lineSeparator() + "new " + tiles2 + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
        server.reset(tiles1 + " " + tiles2, "", "");

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 1);
        String out = clients.get(0).getOutput(0);
        Board b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play(tiles1 + " " + tiles2);

        // P1 plays {8H 8C 8D} {2H 3H 4H}
        out = clients.get(0).getOutput(1);
        b = new Board(out);
        assertEquals(8, b.handSize());
        assertEquals("{*R8 *G8 *O8}", b.getMeld(0));
        assertEquals("{*R2 *R3 *R4}", b.getMeld(1));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Test
    public void testInitialPlay8() {
        String tiles1 = "R2 O2 B2";
        String tiles2 = "G2 G3 G4";
        String tiles3 = "R3 B3 O3";
        String tiles4 = "B5 B6 B7";
        clients.get(0).setInput("new " + tiles1 + System.lineSeparator() + "new " + tiles2 + System.lineSeparator() + "new " + tiles3 + System.lineSeparator() + "new " + tiles4 + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
        server.reset(tiles1 + " " + tiles2 + " " + tiles3 + " " + tiles4, "", "");

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 1);
        String out = clients.get(0).getOutput(0);
        Board b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play(tiles1 + " " + tiles2 + " " + tiles3 + " " + tiles4);

        // P1 plays {2H 2D 2S} {2C 3C 4C} {3H 3S 3D} {5S 6S 7S}
        out = clients.get(0).getOutput(1);
        b = new Board(out);
        assertEquals(2, b.handSize());
        assertEquals("{*R2 *B2 *O2}", b.getMeld(0));
        assertEquals("{*G2 *G3 *G4}", b.getMeld(1));
        assertEquals("{*R3 *B3 *O3}", b.getMeld(2));
        assertEquals("{*B5 *B6 *B7}", b.getMeld(3));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Test
    public void testInitialPlay9() {
        String tiles1 = "R2 B2 G2 O2";
        String tiles2 = "G3 G4 G5 G6 G7";
        String tiles3 = "O4 O5 O6 O7 O8";
        clients.get(0).setInput("new " + tiles1 + System.lineSeparator() + "new " + tiles2 + System.lineSeparator() + "new " + tiles3 + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
        server.reset(tiles1 + " " + tiles2 + " " + tiles3, "", "");

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 1);

        // P1 plays {2H 2S 2C 2D} {3C 4C 5C 6C 7C} {4D 5D 6D 7D 8D}
        String out = clients.get(0).getOutput(1);
        Board b = new Board(out);
        assertEquals(0, b.handSize());
        assertEquals("{*R2 *G2 *B2 *O2}", b.getMeld(0));
        assertEquals("{*G3 *G4 *G5 *G6 *G7}", b.getMeld(1));
        assertEquals("{*O4 *O5 *O6 *O7 *O8}", b.getMeld(2));
        assertEquals(1, b.winner());
    }

    @Test
    public void testNormalPlay1() {
        String tiles1 = "R11 R12 R13 G2 G3 G4";
        String tiles2 = "B11 B12 B13";
        String tiles3 = "O11 O12 O13";
        clients.get(0).setInput("new R11 R12 R13" + System.lineSeparator() + "end" + System.lineSeparator() + "new G2 G3 G4" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("new B11 B12 B13" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(2).setInput("new O11 O12 O13" + System.lineSeparator() + "end" + System.lineSeparator());
        server.reset(tiles1, tiles2, tiles3);

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 4);
        String out = clients.get(0).getOutput(0);
        Board b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play("R11 R12 R13 G2 G3 G4");

        out = clients.get(0).getOutput(4);
        b = new Board(out);
        assertEquals(8, b.handSize());
        assertEquals("{R11 R12 R13}", b.getMeld(0));
        assertEquals("{B11 B12 B13}", b.getMeld(1));
        assertEquals("{O11 O12 O13}", b.getMeld(2));
        assertEquals("{*G2 *G3 *G4}", b.getMeld(3));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Test
    public void testNormalPlay2() {
        String tiles1 = "R11 R12 R13 G2 G3 G4 O8 O9 O10";
        String tiles2 = "B11 B12 B13";
        String tiles3 = "O11 O12 O13";
        clients.get(0).setInput("new R11 R12 R13" + System.lineSeparator() + "end" + System.lineSeparator() + "new G2 G3 G4" + System.lineSeparator() + "new O8 O9 O10" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("new B11 B12 B13" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(2).setInput("new O11 O12 O13" + System.lineSeparator() + "end" + System.lineSeparator());
        server.reset(tiles1, tiles2, tiles3);

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 4);
        String out = clients.get(0).getOutput(0);
        Board b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play("R11 R12 R13 G2 G3 G4 O8 O9 O10");

        out = clients.get(0).getOutput(4);
        b = new Board(out);
        assertEquals(5, b.handSize());
        assertEquals("{*G2 *G3 *G4}", b.getMeld(3));
        assertEquals("{*O8 *O9 *O10}", b.getMeld(4));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Test
    public void testNormalPlay3() {
        String tiles1 = "R11 R12 R13 G2 R2 O2";
        String tiles2 = "B11 B12 B13";
        String tiles3 = "O11 O12 O13";
        clients.get(0).setInput("new R11 R12 R13" + System.lineSeparator() + "end" + System.lineSeparator() + "new G2 R2 O2" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("new B11 B12 B13" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(2).setInput("new O11 O12 O13" + System.lineSeparator() + "end" + System.lineSeparator());
        server.reset(tiles1, tiles2, tiles3);

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 4);
        String out = clients.get(0).getOutput(0);
        Board b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play("R11 R12 R13 G2 R2 O2");

        out = clients.get(0).getOutput(4);
        b = new Board(out);
        assertEquals(8, b.handSize());
        assertEquals("{*R2 *G2 *O2}", b.getMeld(3));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Test
    public void testNormalPlay4() {
        String tiles1 = "R11 R12 R13 G2 R2 O2 O8 R8 B8 G8";
        String tiles2 = "B11 B12 B13";
        String tiles3 = "O11 O12 O13";
        clients.get(0).setInput("new R11 R12 R13" + System.lineSeparator() + "end" + System.lineSeparator() + "new G2 R2 O2" + System.lineSeparator() + "new O8 R8 B8 G8" + System.lineSeparator() +  "end" + System.lineSeparator());
        clients.get(1).setInput("new B11 B12 B13" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(2).setInput("new O11 O12 O13" + System.lineSeparator() + "end" + System.lineSeparator());
        server.reset(tiles1, tiles2, tiles3);

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 4);
        String out = clients.get(0).getOutput(0);
        Board b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play("R11 R12 R13 G2 R2 O2 O8 R8 B8 G8");

        out = clients.get(0).getOutput(4);
        b = new Board(out);
        assertEquals(4, b.handSize());
        assertEquals("{*R2 *G2 *O2}", b.getMeld(3));
        assertEquals("{*R8 *G8 *B8 *O8}", b.getMeld(4));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Test
    public void testNormalPlay5() {
        String tiles1 = "R11 R12 R13 G2 R2 O2 O8 O9 O10";
        String tiles2 = "B11 B12 B13";
        String tiles3 = "O11 O12 O13";
        clients.get(0).setInput("new R11 R12 R13" + System.lineSeparator() + "end" + System.lineSeparator() + "new G2 R2 O2" + System.lineSeparator() + "new O8 O9 O10" + System.lineSeparator() +  "end" + System.lineSeparator());
        clients.get(1).setInput("new B11 B12 B13" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(2).setInput("new O11 O12 O13" + System.lineSeparator() + "end" + System.lineSeparator());
        server.reset(tiles1, tiles2, tiles3);

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 4);
        String out = clients.get(0).getOutput(0);
        Board b = new Board(out);
        Hand oldHand = new Hand(b.getHand());
        oldHand.play("R11 R12 R13 G2 R2 O2 O8 O9 O10");

        out = clients.get(0).getOutput(4);
        b = new Board(out);
        assertEquals(5, b.handSize());
        assertEquals("{*R2 *G2 *O2}", b.getMeld(3));
        assertEquals("{*O8 *O9 *O10}", b.getMeld(4));
        assertEquals(oldHand.toString(), b.getHand());
    }

    @Test
    public void testNormalPlay6() {
        String tiles1 = "R11 R12 R13 G2 R2 O2 G3 R3 O3 G8 G9 G10 G11 G12";
        String tiles2 = "B11 B12 B13";
        String tiles3 = "O11 O12 O13";
        clients.get(0).setInput("new R11 R12 R13" + System.lineSeparator() + "end" + System.lineSeparator() + "new G2 R2 O2" + System.lineSeparator() + "new G3 R3 O3" + System.lineSeparator()+ "new G8 G9 G10 G11 G12" + System.lineSeparator() +  "end" + System.lineSeparator());
        clients.get(1).setInput("new B11 B12 B13" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(2).setInput("new O11 O12 O13" + System.lineSeparator() + "end" + System.lineSeparator());
        server.reset(tiles1, tiles2, tiles3);

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 4);

        String out = clients.get(0).getOutput(4);
        Board b = new Board(out);
        assertEquals(0, b.handSize());
        assertEquals("{*R2 *G2 *O2}", b.getMeld(3));
        assertEquals("{*R3 *G3 *O3}", b.getMeld(4));
        assertEquals("{*G8 *G9 *G10 *G11 *G12}", b.getMeld(5));
    }

    @Test
    public void testDraw1() {
        String tiles1 = "G2 R2 O2 G3 R3 O3 O8 O9 O10 R8 R9 R10 G12 R7";
        clients.get(0).setInput("draw" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
        server.reset(tiles1, "", "");

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 1);

        String out = clients.get(0).getOutput(1);
        Board b = new Board(out);
        assertEquals(15, b.handSize());
        String[] tiles = tiles1.split(" ");
        for (String s: tiles) {
            assertTrue(b.getHand().contains(s));
        }
    }

    @Test
    public void testDraw2() {
        String tiles1 = "G2 G2 O2 R3 B3 B3 R5 B6 O7 R9 R10 G11 B12 B13";
        clients.get(0).setInput("draw" + System.lineSeparator());
        clients.get(1).setInput("");
        clients.get(2).setInput("");
        server.reset(tiles1, "", "");

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 1);

        String out = clients.get(0).getOutput(1);
        Board b = new Board(out);
        assertEquals(15, b.handSize());
        String[] tiles = tiles1.split(" ");
        for (String s: tiles) {
            assertTrue(b.getHand().contains(s));
        }
    }

    @Test
    public void testWinner() {
        String tiles1 = "G2 G2 O2 R3 B3 B3 R5 B6 O7 R9 R10 B11 B12 B13";
        String tiles2 = "R2 B2 G2 O2 G3 G4 G6 G7 O4 O5 O6 O7 O8 O9";
        String tiles3 = "R4 O6 O6 B7 R7 G8 R10 R11 R12 R13 B10 B11 B12 B13";
        clients.get(0).setInput("draw" + System.lineSeparator() + "new G2 O2 R2" + System.lineSeparator() + "new B11 B12 B13" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(1).setInput("draw" + System.lineSeparator() + "new R2 B2 G2 O2" + System.lineSeparator() + "new G3 G4 G5 G6 G7" + System.lineSeparator() + "new O4 O5 O6 O7 O8 O9" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(2).setInput("new R10 R11 R12 R13" + System.lineSeparator() + "new B10 B11 B12 B13" + System.lineSeparator() + "end" + System.lineSeparator());
        server.reset(tiles1, tiles2, tiles3, "G5 R2");

        // waiting for game
        await().until(() -> clients.get(1).outputSize() > 5);

        String out = clients.get(1).getOutput(5);
        Board b = new Board(out);
        assertEquals(0, b.handSize());
        assertEquals("{R10 R11 R12 R13}", b.getMeld(0));
        assertEquals("{B10 B11 B12 B13}", b.getMeld(1));
        assertEquals("{R2 G2 O2}", b.getMeld(2));
        assertEquals("{B11 B12 B13}", b.getMeld(3));
        assertEquals("{*R2 *G2 *B2 *O2}", b.getMeld(4));
        assertEquals("{*G3 *G4 *G5 *G6 *G7}", b.getMeld(5));
        assertEquals("{*O4 *O5 *O6 *O7 *O8 *O9}", b.getMeld(6));
        assertEquals(2, b.winner());
        assertEquals(-48, b.getScore(0));
        assertEquals(0, b.getScore(1));
        assertEquals(-38, b.getScore(2));
    }

    @Test
    public void testAddTiles() {
        String tiles1 = "R11 R12 R13 B9 O9 O10";
        String tiles2 = "B10 B11 B12";
        String tiles3 = "O11 O12 O13";
        clients.get(0).setInput("new R11 R12 R13" + System.lineSeparator() + "end" + System.lineSeparator() + "add 2 B9" + System.lineSeparator() + "add 3 O9 O10" + System.lineSeparator() +  "end" + System.lineSeparator());
        clients.get(1).setInput("new B10 B11 B12" + System.lineSeparator() + "end" + System.lineSeparator());
        clients.get(2).setInput("new O11 O12 O13" + System.lineSeparator() + "end" + System.lineSeparator());
        server.reset(tiles1, tiles2, tiles3);

        // waiting for game
        await().until(() -> clients.get(0).outputSize() > 4);

        String out = clients.get(0).getOutput(4);
        Board b = new Board(out);
        assertEquals(8, b.handSize());
        assertEquals("{*B9 B10 B11 B12}", b.getMeld(1));
        assertEquals("{*O9 *O10 O11 O12 O13}", b.getMeld(2));
    }

    @AfterEach
    public void close() throws IOException {
        server.close();
        server = null;
        clients.clear();
    }
}
