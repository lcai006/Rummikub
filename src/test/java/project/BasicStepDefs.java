package project;

import java.io.ByteArrayInputStream;

import io.cucumber.java8.En;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicStepDefs implements En {
    LocalGame game;
    String tile1;
    String tile2;
    String tile3;
    String output;

    public BasicStepDefs() {
        game = new LocalGame();
        StringBuilder inString = new StringBuilder();
        tile1 = "";
        tile2 = "";
        tile3 = "";

        Given("^Players start the game$", () -> game.reset(tile1, tile2, tile3));

        When("^Player (\\d+) draws a tile$", (Integer arg0) ->
            inString.append("draw").append(System.lineSeparator()));

        When("^Player (\\d+) enters wrong action$", (Integer arg0) ->
            inString.append("end").append(System.lineSeparator()));

        Then("^Game shows result$", () -> {
            game.setInput(inString.toString());
            System.out.println(inString);
            inString.setLength(0);
            game.start();
            output = game.getOutput();
        });

        Then("^Player (\\d+) has (\\d+) tiles$", (Integer arg0, Integer arg1) ->
            assertEquals(arg1, game.getHand(arg0 - 1).size()));

        Then("^Next Player is Player (\\d+)$", (Integer arg0) ->
            assertEquals(arg0, game.getCurrentPlayer() + 1));

        Then("^Player receives an error message$", () ->
            assertEquals("Error found, try again", game.error()));

        Given("^Player (\\d+) has tiles: (.*+)$", (Integer arg0, String tiles) -> {
            switch (arg0) {
                case 1:
                    tile1 = tiles;
                case 2:
                    tile2 = tiles;
                case 3:
                    tile3 = tiles;
            }
        });

        When("^Player (\\d+) plays (.*+)$", (Integer arg0, String melds) -> {
            for(String meld: melds.split(",")) {
                inString.append("new ").append(meld).append(System.lineSeparator());
            }
            inString.append("end").append(System.lineSeparator());
        });

        Then("^Table has melds (.*+)$", (String melds) -> {
            for(String meld: melds.split(",")) {
                assertTrue(game.getTable().toString().contains(meld));
            }
        });

        Then("^Player (\\d+) receives an penalty for invalid move$", (Integer arg0) -> assertEquals("Invalid Move, Player 1 draws 3 tiles", game.error()));

        Then("^Player (\\d+) receives an penalty for invalid initial play$", (Integer arg0) -> assertEquals("Need 30 points for initial play", game.error()));

    }
}
