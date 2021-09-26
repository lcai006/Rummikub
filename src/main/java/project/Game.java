package project;

import model.*;

import java.util.ArrayList;

public class Game {
    private int currentPlayer;
    private Deck deck;
    private Table table;
    private ArrayList<Hand> hands;
    private boolean isEnd;
    private ArrayList<Boolean> isInitPlay;

    public Game() {
        reset("", "", "");
        isEnd = false;
    }

    // reset game
    public void reset(String hand1, String hand2, String hand3) {
        isEnd = false;
        currentPlayer = 0;
        deck = new Deck();
        table = new Table();
        hands = new ArrayList<>();
        Hand h = new Hand(listToString(deck.createHand(hand1)));
        hands.add(h);
        h = new Hand(listToString(deck.createHand(hand2)));
        hands.add(h);
        h = new Hand(listToString(deck.createHand(hand3)));
        hands.add(h);
        isInitPlay = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            isInitPlay.add(true);
        }
    }

    public static String listToString(ArrayList<String> list) {
        StringBuilder result = new StringBuilder();
        for (String s: list) {
            if (!result.isEmpty()) {
                result.append(" ");
            }
            result.append(s);
        }
        return result.toString();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public String getOutput(int player) {
        return "Player " + (currentPlayer + 1) + "’s turn\n\n" +
                "Table\n" +
                table.toString() +
                "\nHand\n" +
                hands.get(player).display();
    }

    public void action(String act) {
        table.removeHighlight();
        String[] lines = act.split("\n");

        if (isInitPlay.get(currentPlayer)) {
            if (lines[0].length() > 4) {
                if (!checkInit(lines)) {
                    return;
                }
            }
        }

        for (String line: lines) {
            if (line.equals("draw")) {
                draw();
            } else {
                String[] str = line.split(" ", 2);
                if (str[0].equals("new")) {
                    String tiles = line.substring(4);
                    newMeld(tiles);
                }
            }
        }

        if (currentPlayer == 2) {
            currentPlayer = 0;
        } else {
            currentPlayer++;
        }
    }

    // current player draws a tile
    public void draw() {
        String tile = deck.draw();
        hands.get(currentPlayer).add(tile);
    }

    // current player plays a new meld
    public void newMeld(String tiles) {
        table.createMeld(tiles);
        table.newHighLight(table.size() - 1);
        hands.get(currentPlayer).play(tiles);
    }

    // check initial play with at least 30 points
    public boolean checkInit(String[] lines) {
        int score = 0;
        for (String line: lines) {
            String[] str = line.split(" ", 2);
            if (str[0].equals("new")) {
                String tiles = line.substring(4);
                Meld m = new Meld(tiles);
                score += m.score();
            }
        }

        return score >= 30;
    }

    public boolean isEnd() {
        return isEnd;
    }
}
