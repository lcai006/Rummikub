package model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private final ArrayList<String> deck = new ArrayList<>();

    public Deck() {
        // deck is a list of tiles in string
        String[] colors = {"R", "G", "B", "O"};
        for (String color: colors) {
            for (int i = 1; i <= 13; i++) {
                deck.add(color+i);
                deck.add(color+i);
            }
        }
        deck.add("Joker");
        deck.add("Joker");

        // Randomize the deck
        Collections.shuffle(deck);
    }

    public int size() {
        return deck.size();
    }

    // Generate hand list with or without predefined tiles, predefined tiles are used for testing
    public ArrayList<String> createHand(String predefined) {
        ArrayList<String> hand = new ArrayList<>();

        if (predefined.equals("")) {
            // draws 14 tiles
            for (int i = 0; i < 14; i++) {
                if (deck.size() > 0) {
                    hand.add(deck.get(0));
                    deck.remove(0);
                }
            }
        } else {
            // adds predefined tiles
            String[] tiles = predefined.split("\\s+");
            for (String tile : tiles) {
                hand.add(tile);
                deck.remove(tile);
            }
            // draws left tiles
            for (int i = 0; i < 14 - tiles.length; i++) {
                if (deck.size() > 0) {
                    hand.add(deck.get(0));
                    deck.remove(0);
                }
            }
        }

        return hand;
    }

    // Draw a tile from the deck
    public String draw() {
        String tile = deck.get(0);
        deck.remove(0);
        return tile;
    }

    // Set rigged tiles in reversed order
    public void set(String list) {
        String[] tiles= list.split(" ");
        for (String t: tiles) {
            deck.remove(t);
        }

        for (String t: tiles) {
            deck.add(0, t);
        }
    }

}
