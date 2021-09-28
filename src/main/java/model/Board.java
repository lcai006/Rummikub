package model;

import java.util.ArrayList;
import java.util.Collections;

public class Board {
    private String turn;
    private ArrayList<String> oriMelds;
    private ArrayList<String> melds;
    private StringBuilder hand;
    private int winner;
    private ArrayList<Integer> scores;

    /**
     * parse outputs text to object attributes for testing
     *
     */
    public Board(String output) {
        turn = "";
        winner = -1;
        oriMelds = new ArrayList<>();
        melds = new ArrayList<>();
        hand = new StringBuilder();
        scores = new ArrayList<>();

        String[] lines = output.split("\n");
        String label = "";
        for (String line: lines) {
            if (turn.equals("") && line.length() == 15) {
                turn = line;
            } else if (line.equals("Table")) {
                label = "Table";
                continue;
            } else if (line.equals("Hand")) {
                label = "Hand";
                continue;
            } else if (line.startsWith("Winner")) {
                winner = Integer.parseInt(line.substring(line.length() - 1));
                continue;
            } else if (line.equals("Scores")) {
                label = "Scores";
                continue;
            } else if (line.equals("")) {
                label = "";
                continue;
            }

            switch (label) {
                case "Table" -> {
                    line = line.split(": ")[1];
                    oriMelds.add(line);
                    line = line.replace("*", "");
                    line = line.replace("!", "");
                    line = line.replace("{", "");
                    line = line.replace("}", "");
                    melds.add(line);
                }
                case "Hand" -> {
                    if (!hand.isEmpty()) {
                        hand.append(" ");
                    }
                    hand.append(line);
                }
                case "Scores" -> {
                    line = line.replace("P1: ", "");
                    line = line.replace("P2: ", "");
                    line = line.replace("P3: ", "");
                    String[] s = line.split(" ");
                    scores.add(Integer.parseInt(s[0]));
                    scores.add(Integer.parseInt(s[1]));
                    scores.add(Integer.parseInt(s[2]));
                }
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
        return winner;
    }

    public int getScore(int player) {
        return scores.get(player);
    }
}
