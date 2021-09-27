package model;

import java.util.ArrayList;
import java.util.Collections;

public class Board {
    private String turn;
    private ArrayList<String> oriMelds;
    private ArrayList<String> melds;
    private StringBuilder hand;

    /**
     * parse outputs text to object attributes for testing
     *
     */
    public Board(String output) {
        turn = "";
        oriMelds = new ArrayList<>();
        melds = new ArrayList<>();
        hand = new StringBuilder();

        String[] lines = output.split("\n");
        String label = "";
        for (String line: lines) {
            if (turn.equals("")) {
                turn = line;
            } else if (line.equals("Table")) {
                label = "Table";
                continue;
            } else if (line.equals("Hand")) {
                label = "Hand";
                continue;
            } else if (line.equals("")) {
                label = "";
                continue;
            }

            if (label.equals("Table")) {
                line = line.split(": ")[1];
                oriMelds.add(line);
                line = line.replace("*", "");
                line = line.replace("!", "");
                line = line.replace("{", "");
                line = line.replace("}", "");
                melds.add(line);
            } else if (label.equals("Hand")) {
                if (!hand.isEmpty()) {
                    hand.append(" ");
                }
                hand.append(line);
            }
        }
    }

    // return the information that indicates who's turn
    public String turnInfo() {
        return turn;
    }

    // return the size of table (melds)
    public int tableSize() {
        return oriMelds.size();
    }

    // return the size of hand
    public int handSize() {
        if (hand.isEmpty()) {
            return 0;
        }
        Hand h = new Hand(hand.toString());
        return h.size();
    }

    // return the number of exact melds in the table
    public int countMeld(String meld) {
        return Collections.frequency(melds, meld);
    }

    // get original meld text by number
    public String getMeld(int i) {
        return oriMelds.get(i);
    }

    // get hand in text
    public String getHand() {
        return hand.toString();
    }

    public int winner() {
        return 0;
    }

    public int getScore(int player) {
        return 0;
    }
}
