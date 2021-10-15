package project;

import model.Hand;
import model.Table;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
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

                    String token = input.split(" ")[0];
                    String[] list = {"draw", "new", "add", "reuse"};
                    if (!Arrays.asList(list).contains(token)) {
                        errorInfo = "Error found, try again";
                        System.out.println(errorInfo);
                        continue;
                    }

                    game.action(action.toString());
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
}
