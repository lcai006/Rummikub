package project;

import model.Hand;
import model.Table;

import java.util.ArrayList;
import java.util.Scanner;

public class LocalGame {
    private final Game game;
    private String output;
    private String errorInfo;
    private Scanner scan;

    public LocalGame() {
        game = new Game();
        output = "";
        scan = new Scanner(System.in);
    }

    public static void main(String[] args) {
        LocalGame game = new LocalGame();
        game.start();
    }

    public void start() {
        // Game logic
        while (!game.isEnd()) {
            output = game.getOutput(game.getCurrentPlayer());
            System.out.println(output);
            System.out.println("Your action:");

            // Reads player's action
            StringBuilder action = new StringBuilder();
            String input;
            int err = 1;

            while (err != 0) {
                err = 0;
                if (scan.hasNext()) {
                    input = scan.nextLine();

                    if (input.equals("draw")) {
                        action.append(input);
                    } else {
                        while (!input.equals("end")) {
                            action.append(input).append("\n");
                            input = scan.nextLine();
                        }
                    }
                    for (String line: action.toString().split("\n")) {
                        if (!checkAction(line)) {
                            err = 1;
                        }
                    }
                    if (err == 1) {
                        errorInfo = "Error found, try again";
                        System.out.println(errorInfo);
                        continue;
                    }

                    game.action(action.toString());
                    errorInfo = game.getError();
                    if (!errorInfo.equals("")) {
                        System.out.println(errorInfo);
                    }
                } else {
                    return;
                }
            }

        }

        System.out.println(game.winInfo());
        output = game.winInfo();
        scan.close();
    }

    public void reset(String hand1, String hand2, String hand3) {
        game.reset(hand1, hand2, hand3);
    }

    public Hand getHand(int i) {
        return game.getHand(i);
    }

    public Table getTable() {
        return game.getTable();
    }

    public String getOutput() {
        return output;
    }

    public int getCurrentPlayer() {
        return game.getCurrentPlayer();
    }

    public void setInput(String input) {
        scan = new Scanner(input);
    }

    public String error() {
        return errorInfo;
    }

    private boolean checkAction(String action) {
        String token = action.split("\\s+")[0];
        String[] str;
        int num;
        String tiles;
        switch (token) {
            case "draw" -> {
                return action.length() == 4;
            }
            case "new" -> {
                if (action.split("\\s+").length >= 4) {
                    return checkTiles(action.substring(4));
                } else {
                    return false;
                }
            }
            case "add" -> {
                str = action.split("\\s+");
                if (!str[1].matches("^[1-9]\\d*$")) {
                    return false;
                }

                num = Integer.parseInt(str[1]);

                if (action.length() > 7) {
                    if (num < 10) {
                        tiles = action.substring(6);
                    } else {
                        tiles = action.substring(7);
                    }
                } else {
                    return false;
                }

                if (action.split("\\s+").length >= 3) {
                    return checkTiles(tiles);
                } else {
                    return false;
                }
            }
            case "reuse" -> {
                str = action.split("\\s+");
                if (!str[1].matches("^[1-9]\\d*$")) {
                    return false;
                }

                num = Integer.parseInt(str[1]);

                if (action.length() > 9) {
                    if (num < 10) {
                        tiles = action.substring(8);
                    } else {
                        tiles = action.substring(9);
                    }
                } else {
                    return false;
                }

                if (action.split("\\s+").length >= 3) {
                    return checkTiles(tiles.replace("with ", ""));
                } else {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
    }

    private boolean checkTiles(String tiles) {
        ArrayList<String> list = new ArrayList<>();
        String[] colors = {"R", "G", "B", "O"};
        for (String color: colors) {
            for (int i = 1; i <= 13; i++) {
                list.add(color+i);
            }
        }
        list.add("Joker");

        for (String tile: tiles.split("\\s+")) {
            if (tile.contains("Joker=")) {
                if (!list.contains(tile.split("=")[1])) {
                    return false;
                }
                continue;
            }
            if (!list.contains(tile)) {
                return false;
            }
        }

        return true;
    }

    public int getWinner() {
        return game.getWinner();
    }

    public int getScores(int i) {
        return game.getScores(i);
    }
}
