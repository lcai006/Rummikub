import model.Deck;
import model.Hand;
import model.Tile;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @DisplayName("test the deck creation")
    @Test
    public void testDeck() {
        Deck deck = new Deck();
        assertEquals(104, deck.size());
    }

    @DisplayName("test normal and rigged hand list creation")
    @Test
    public void testRiggedHandListCreation() {
        Deck deck = new Deck();
        String list1 = "R11 B11 G11 R3 R4 R5 B1 B2 B3 B4 G4 B4 O5 O13";
        String list2 = "R12 B12 G12 O12";
        ArrayList<String> hand1 = deck.createHand(list1);
        ArrayList<String> hand2 = deck.createHand(list2);
        ArrayList<String> hand3 = deck.createHand("");
        assertEquals(14, hand1.size());
        String[] tiles1 = list1.split("\\s+");
        for(String tile: tiles1) {
            assertTrue(hand1.contains(tile));
        }

        assertEquals(14, hand2.size());
        String[] tiles2 = list2.split("\\s+");
        for(String tile: tiles2) {
            assertTrue(hand2.contains(tile));
        }

        assertEquals(14, hand3.size());

        assertEquals(62, deck.size());
    }

    @DisplayName("test drawing a tile at a time from the deck")
    @Test
    public void testDrawTile() {
        Deck deck = new Deck();
        deck.createHand("");
        deck.createHand("");
        deck.createHand("");
        deck.draw();
        assertEquals(61, deck.size());
        deck.draw();
        assertEquals(60, deck.size());
    }

    @DisplayName("test tile object, attributes and display in text")
    @Test
    public void testTile() {
        Tile tile1 = new Tile("O3");
        assertEquals("O", tile1.color());
        assertEquals(3, tile1.number());
        assertEquals("O3", tile1.toString());

        Tile tile2 = new Tile("R12");
        tile2.newHighlight();
        assertEquals("R", tile2.color());
        assertEquals(12, tile2.number());
        assertEquals("*R12", tile2.toString());


        Tile tile3 = new Tile("B7");
        tile3.moveHighlight();
        assertEquals("B", tile3.color());
        assertEquals(7, tile3.number());
        assertEquals("!B7", tile3.toString());
    }

    @DisplayName("test hand object, display in order")
    @Test
    public void testHandCreation() {
        String tiles = "O5 G4 R1";
        Hand hand = new Hand(tiles);
        assertEquals(3, hand.size());
        assertEquals("R1 G4 O5", hand.toString());
        assertEquals("R1\nG4\nO5\n", hand.display());

        tiles = "B2 B1 O10 R11 B7";
        hand = new Hand(tiles);
        assertEquals(5, hand.size());
        assertEquals("R11 B1 B2 B7 O10", hand.toString());
        assertEquals("R11\nB1 B2 B7\nO10\n", hand.display());
    }

    @DisplayName("test add and remove tiles in hand")
    @Test
    public void testHandModification() {
        String tiles = "B2 B3 B4 O10 R11";
        Hand hand = new Hand(tiles);

        hand.play("O10");
        assertEquals(4, hand.size());
        assertEquals("R11 B2 B3 B4", hand.toString());

        hand.add("R5");
        assertEquals(5, hand.size());
        assertEquals("R5 R11 B2 B3 B4", hand.toString());

        hand.add("B3");
        assertEquals(6, hand.size());
        assertEquals("R5 R11 B2 B3 B3 B4", hand.toString());

        hand.play("B2 B3 B4");
        assertEquals(3, hand.size());
        assertEquals("R5 R11 B3", hand.toString());
    }
}
