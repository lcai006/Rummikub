package project;

import model.*;

import java.util.ArrayList;

public class Game {
    private int currentPlayer;
    private Deck deck;
    private Table table;
    private ArrayList<Hand> hands;
    private int winner;
    private ArrayList<Boolean> isInitPlay;
    private ArrayList<String> tempTiles;
    private String err;

    public Game() {
        reset("", "", "");
        winner = -1;
    }

    // reset game
    public void reset(String hand1, String hand2, String hand3) {
        winner = -1;
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
        tempTiles = new ArrayList<>();
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
        StringBuilder output = new StringBuilder();
        if (!isEnd()) {
            output.append("Player ").append(currentPlayer + 1).append("’s turn\n\n");
        }
        output.append("Table\n").append(table.toString()).append("\nHand\n").append(hands.get(player).display());

        if (isEnd()) {
            output.append("\n").append(winInfo());
        }

        return output.toString();
    }

    public String winInfo() {

        return "Winner: Player " + (winner + 1) + "\n" +
                "Scores\n" +
                "P1: " + hands.get(0).score() + " P2: " + hands.get(1).score() + " P3: " + hands.get(2).score() + "\n";
    }

    public void action(String act) {
        err = "";
        table.removeHighlight();
        String[] lines = act.split("\n");

        if (isInitPlay.get(currentPlayer)) {
            if (lines[0].length() > 4) {
                if (!checkInit(lines)) {
                    penalty();
                    err = "Need 30 points for initial play";
                    nextPlayer();
                    return;
                }
                isInitPlay.set(currentPlayer, false);
            }
        }

        for (String line: lines) {
            String[] str = line.split("\\s+", 3);
            switch (str[0]) {
                case "new" -> {
                    String tiles = line.substring(4);
                    if (notInHand(tiles) || !checkMeld(tiles)) {
                        penalty();
                        nextPlayer();
                        return;
                    }
                }
                case "add" -> {
                    int num = Integer.parseInt(str[1]);
                    String tiles;
                    if (Integer.parseInt(str[1]) < 10) {
                        tiles = line.substring(6);
                    } else {
                        tiles = line.substring(7);
                    }
                    Meld m = table.getMeld(num - 1);
                    m.add(tiles);
                    if (notInHand(tiles) || m.invalid("")) {
                        penalty();
                        nextPlayer();
                        return;
                    }
                }
                case "reuse" -> {
                    int num = Integer.parseInt(str[1]);
                    String tiles;
                    if (Integer.parseInt(str[1]) < 10) {
                        tiles = line.substring(8);
                    } else {
                        tiles = line.substring(9);
                    }
                }
            }
        }

        for (String line: lines) {
            if (line.equals("draw")) {
                draw();
            } else {
                String[] str = line.split("\\s+", 3);
                switch (str[0]) {
                    case "new" -> {
                        String tiles = line.substring(4);
                        newMeld(tiles);
                    }
                    case "add" -> {
                        int num = Integer.parseInt(str[1]);
                        String tiles;
                        if (Integer.parseInt(str[1]) < 10) {
                            tiles = line.substring(6);
                        } else {
                            tiles = line.substring(7);
                        }
                        addToMeld(num - 1, tiles);
                    }
                    case "reuse" -> {
                        int num = Integer.parseInt(str[1]);
                        String tiles;
                        if (Integer.parseInt(str[1]) < 10) {
                            tiles = line.substring(8);
                        } else {
                            tiles = line.substring(9);
                        }

                        reuseTiles(num - 1, tiles);
                    }
                }
            }
        }

        table.checkMelds();

        if (hands.get(currentPlayer).size() == 0) {
            winner = currentPlayer;
        }

        nextPlayer();
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
        Hand h = new Hand(tiles);
        for (String t: tiles.split("\\s+")) {
            if (tempTiles.contains(t)) {
                tempTiles.remove(t);
                table.moveHighLight(table.size() - 1, t);
                h.play(t);
            }
        }
        if (!h.toString().isEmpty()) {
            hands.get(currentPlayer).play(h.toString());
        }
    }

    // current player add tiles to a meld
    public void addToMeld(int i, String tiles) {
        table.addTile(i, tiles);
        table.newHighLight(i, tiles);
        Hand h = new Hand(tiles);
        for (String t: tiles.split("\\s+")) {
            if (tempTiles.contains(t)) {
                tempTiles.remove(t);
                table.moveHighLight(i, t);
                h.play(t);
            }
        }
        if (!h.toString().isEmpty()) {
            hands.get(currentPlayer).play(h.toString());
        }
    }

    // check initial play with at least 30 points
    public boolean checkInit(String[] lines) {
        int score = 0;
        for (String line: lines) {
            String[] str = line.split("\\s+", 2);
            if (str[0].equals("new")) {
                String tiles = line.substring(4);
                Meld m = new Meld(tiles);
                score += m.score();
            } else if (str[0].equals("add") || str[0].equals("reuse")) {
                if (score < 30)
                    return false;
            }
        }

        return score >= 30;
    }

    // change to next player
    public void nextPlayer() {
        if (currentPlayer == 2) {
            currentPlayer = 0;
        } else {
            currentPlayer++;
        }
    }

    public void reuseTiles(int i, String tiles) {
        for (String t: tiles.split(" ")) {
            tempTiles.add(t);
            table.removeTile(i, t);
        }
    }

    public void setDeck(String tilesToDraw) {
        deck.set(tilesToDraw);
    }

    public boolean isEnd() {
        return winner != -1;
    }

    public Hand getHand(int i) {
        return hands.get(i);
    }

    public Table getTable() {
        return table;
    }

    public void penalty() {
        err = "Invalid Move, Player " + (currentPlayer+1) + " draws 3 tiles";
        for (int i = 0; i < 3; i++) {
            draw();
        }
    }

    public String getError() {
        return err;
    }

    // Check if tiles are in hand
    public boolean notInHand(String tiles) {
        Hand h = hands.get(currentPlayer);
        for (String tile: tiles.split("\\s+")) {
            if (!h.toString().contains(tile)) {
                return true;
            }
        }

        return false;
    }

    // Check if tiles are in hand
    public boolean checkMeld(String tiles) {
        Meld m = new Meld(tiles);
        return !m.invalid("");
    }
}
