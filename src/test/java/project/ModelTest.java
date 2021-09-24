package project;

import model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


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

    @DisplayName("test meld creation, modification and display in text")
    @Test
    public void testMeld() {
        String tiles = "B2 B4 B3";
        Meld m1 = new Meld(tiles);
        assertEquals(3, m1.size());
        assertEquals("run", m1.type());
        assertEquals("{B2 B3 B4}", m1.toString());

        m1.add("B5");
        assertEquals(4, m1.size());
        assertEquals("{B2 B3 B4 B5}", m1.toString());

        tiles = "G12 R12 O12 B12";
        Meld m2 = new Meld(tiles);
        assertEquals(4, m2.size());
        assertEquals("set", m2.type());
        assertEquals("{R12 G12 B12 O12}", m2.toString());
        m2.remove("R12");
        assertEquals("{G12 B12 O12}", m2.toString());

        tiles = "R12 R1 R13";
        Meld m3 = new Meld(tiles);
        assertEquals(3, m3.size());
        assertEquals("run", m3.type());
        assertEquals("{R12 R13 R1}", m3.toString());
    }

    @DisplayName("test table creation, modification of melds and display in text")
    @Test
    public void testTable() {
        Table table = new Table();
        assertTrue(table.isEmpty());

        String tiles = "B11 B12 B13";
        table.createMeld(tiles);
        assertFalse(table.isEmpty());
        assertEquals("Meld 1: {B11 B12 B13}\n", table.toString());

        tiles = "G12 R12 O12 B12";
        table.createMeld(tiles);
        assertEquals("Meld 1: {B11 B12 B13}\nMeld 2: {R12 G12 B12 O12}\n", table.toString());
        table.removeTile(2, "R12");
        assertEquals("Meld 1: {B11 B12 B13}\nMeld 2: {G12 B12 O12}\n", table.toString());

        table.addTile(1, "B1 B10");
        assertEquals("Meld 1: {B10 B11 B12 B13 B1}\nMeld 2: {G12 B12 O12}\n", table.toString());
    }
}
