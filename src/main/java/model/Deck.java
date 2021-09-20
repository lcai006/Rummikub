package model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<String> deck = new ArrayList<>();

    public Deck() {
        // deck is a list of tiles in string
        String[] colors = {"R", "G", "B", "O"};
        for (String color: colors) {
            for (int i = 1; i <=13; i++) {
                deck.add(color+i);
                deck.add(color+i);
            }
        }

        // Randomize the deck
        Collections.shuffle(deck);
    }

    public int size() {
        return deck.size();
    }

    // Generate hand list with or without predefined tiles, predefined tiles are used for testing
    public ArrayList<String> createHand(String predefined) {
        ArrayList<String> hand = new ArrayList<>();

        return hand;
    }

}
